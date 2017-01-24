package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.model.ContainerBase;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.service.ContainerService;
import cn.aiyuedu.bs.wap.service.ParamService;
import com.duoqu.commons.web.spring.RequestAttribute;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/container/*")
public class ContainerController {

    @Autowired
    private ContainerService containerService;
    @Autowired
    private ParamService paramService;

    @RequestMapping("show.do")
    public String show(
            HttpServletRequest request,
            @RequestAttribute(value = "param", required = true) ParamDto param,
            @SessionAttribute(value = "user", required = false) User user,
            @RequestParam(value = "id", required = true) Integer id,
            ModelMap model) {
        ContainerBase container = containerService.getContainer(id);
        model.put("container", container);
        model.put("id", id);
        model.put("user", user);

        param.setPageId(id);
        paramService.store(request, param);

        return "/page/container.html";
    }
}
