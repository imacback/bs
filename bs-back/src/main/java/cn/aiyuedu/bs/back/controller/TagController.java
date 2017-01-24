package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.TagService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.TagDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Tag;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/tag/*")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<TagDto> list(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "typeId", required = false) Integer typeId,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "isLeaf", required = false) Integer isLeaf,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        if (StringUtils.isEmpty(name)) {
            name = null;
        }
        return tagService.getPage(id, null, name, parentId, isLeaf, typeId, start, limit);
    }

    @RequestMapping("subList.do")
    @ResponseBody
    public Map<String, Object> subList(
            @RequestParam(value = "typeId", required = false) Integer typeId) {
        Map<String, Object> responseMap = new HashMap<>();
        List<Tag> list = tagService.getTagsByTypeId(typeId);
        responseMap.put("success", "true");
        responseMap.put("result", list);
        return responseMap;
    }

    @RequestMapping("parentList.do")
    @ResponseBody
    public Map<String, Object> parentList() {
        Map<String, Object> responseMap = new HashMap<>();
        List<Tag> list = tagService.getParentTagList();
        responseMap.put("success", "true");
        responseMap.put("result", list);
        return responseMap;
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("tag") Tag tag,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = tagService.save(tag, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("publish.do")
    @ResponseBody
    public Map<String, Object> publish(
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = tagService.publish();

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("del.do")
    @ResponseBody
    public Map<String, Object> del(
            @RequestParam(value = "ids", required = false) String ids,
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotBlank(ids)) {
            List<Integer> list = StringUtil.split2Int(ids);

            ResultDto resultDto = tagService.delete(list);

            responseMap.put("success", resultDto.getSuccess()? "true": "false");
            responseMap.put("info", resultDto.getInfo());
        } else {
            responseMap.put("info", "请选择要删除的标签！");
        }

        return responseMap;
    }
}
