package com.majiangcoummunity.majiangcommunity.Controller;

import com.majiangcoummunity.majiangcommunity.Provider.GitHubProvider;
import com.majiangcoummunity.majiangcommunity.dto.AccessTokenDTO;
import com.majiangcoummunity.majiangcommunity.dto.GitHubUser;
import com.majiangcoummunity.majiangcommunity.mapper.UserMapper;
import com.majiangcoummunity.majiangcommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {


    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String index(@RequestParam(name = "code") String code,
                        @RequestParam(name = "state") String state,
                        HttpServletRequest request,
                        HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_secret(clientSecret);

        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
        if(gitHubUser != null){
            User user = new User();
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            //ctrl+alt+v 将变量分离出来
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //手动写入cookie
            response.addCookie(new Cookie("token", token));

            //登录成功  写cookie和session
            request.getSession().setAttribute("user", gitHubUser);
            return "redirect:/";
        }else{
            //登录失败  重新登陆
            return "redirect:/";

        }
    }

}
