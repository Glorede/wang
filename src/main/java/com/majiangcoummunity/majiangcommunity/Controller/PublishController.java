package com.majiangcoummunity.majiangcommunity.Controller;

import com.majiangcoummunity.majiangcommunity.mapper.QuestionMapper;
import com.majiangcoummunity.majiangcommunity.mapper.UserMapper;
import com.majiangcoummunity.majiangcommunity.model.Question;
import com.majiangcoummunity.majiangcommunity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model){

        //属性放到model方便publish.html前端直接获取属性
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title.equals("")){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if(description.equals("")){
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }if(description.equals("")){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }


        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        if(user == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }


        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmt_create(System.currentTimeMillis());
        question.setGmt_modified(question.getGmt_create());


        questionMapper.create(question);
        return "redirect:/";
    }
}
