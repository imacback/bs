package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.component.BookComponent;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.da.enumeration.ConfigEnum;
import cn.aiyuedu.bs.da.model.CpBook;
import cn.aiyuedu.bs.da.service.KanshuService;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/bookSnatch/*")
public class BookSnatchController {

    @Autowired
    private BookComponent bookComponent;

    @RequestMapping("kanshu.do")
    @ResponseBody
    public Map<String, Object> kanshu() {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        bookComponent.snatchKanshu();

        responseMap.put("success", "true");
        responseMap.put("info", "已发起书籍抓取，请稍后！");

        return responseMap;
    }
}
