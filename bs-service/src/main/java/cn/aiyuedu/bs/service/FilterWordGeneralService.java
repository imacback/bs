package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.FilterReplaceStrategyType;
import cn.aiyuedu.bs.common.Constants.FilterType;
import cn.aiyuedu.bs.common.Constants.FilterWordLevel;
import cn.aiyuedu.bs.common.util.CharUtil;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.dto.FilterWordQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterWordResultDto;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.dao.mongo.repository.FilterWordRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
public class FilterWordGeneralService {

    private final Logger logger = LoggerFactory.getLogger(FilterWordGeneralService.class);

    private Map<String, FilterWord> filterWordMap = Maps.newHashMap();

    @Autowired
    private FilterWordRepository filterWordRepository;

    public synchronized void reload() {

        if (logger.isDebugEnabled()) {
            logger.debug("FilterWord reload");
        }

        FilterWordQueryDto queryDto = new FilterWordQueryDto();
        queryDto.setStatus(1);
        List<FilterWord> filterWords = filterWordRepository.find(queryDto);
        if (CollectionUtils.isNotEmpty(filterWords)) {
            filterWordMap.clear();
            for (FilterWord f : filterWords) {
                filterWordMap.put(f.getWord(), f);
            }
        }
    }

    /**
     * 是否含有违禁词
     * @param text
     * @param isComment 是否评论
     * @return
     */
    public boolean containFilterWords(String text, boolean isComment) {
        if (StringUtils.isNotBlank(text)) {
            Collection<FilterWord> filterWords = filterWordMap.values();
            for (FilterWord f : filterWords) {
                if (f != null) {
                    if (isComment && f.getAllowComment() != null && f.getAllowComment() == 1) {
                        continue;
                    }
                    int count = StringUtils.countMatches(text, f.getWord());
                    if (count > 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public FilterResultDto filter(String text, FilterType filterType) {
        if (StringUtils.isNotEmpty(text)) {
            text = StringUtil.removeHtmlFontTag(text);
            FilterResultDto r = new FilterResultDto();
            r.setOriginText(text);
            Map<Integer, List<FilterWordResultDto>> map = Maps.newHashMap();
            Map<String, Integer> resultMap;
            Integer level, amount, totalCount = 0;
            String content = text;
            String word;
            List<FilterWordResultDto> list;
            FilterWordResultDto filterWordResultDto;
            Collection<FilterWord> filterWords = filterWordMap.values();
            for (FilterWord f : filterWords) {
                word = f.getWord();
                int count = StringUtils.countMatches(content, word);
                if (count > 0) {
                    level = f.getLevel();
                    list = map.get(level);
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    if (filterType == FilterType.Highlight) {
                        content = StringUtils.replace(content, word, getHighlight(word));
                    } else if (filterType == FilterType.Replace) {
                        if (f.getReplaceStrategyType() == FilterReplaceStrategyType.Normal.getId()) {
                            content = StringUtils.replace(content, word, getReplace(word));
                        } else if (f.getReplaceStrategyType() == FilterReplaceStrategyType.Specify.getId()) {
                            content = StringUtils.replace(content, word, f.getReplaceWord());
                        }
                    }
                    r.setReplaced(true);

                    filterWordResultDto = new FilterWordResultDto(f, count);
                    list.add(filterWordResultDto);

                    totalCount = totalCount + count;

                    if (CollectionUtils.isNotEmpty(list)) {
                        map.put(level, list);
                    }
                }
            }

            if (totalCount > 0) {
                r.setIsPassed(false);
            } else {
                r.setIsPassed(true);
            }
            r.setResult(map);
            if (r.getReplaced()) {
                r.setText(content);
            } else {
                r.setText(r.getOriginText());
            }
            r.setChineseLength(CharUtil.countChinese(r.getText()));
            return r;
        }

        return null;
    }

    public FilterWord getByWord(String word) {
        return filterWordMap.get(word);
    }

    public String getReplace(String filterWord) {
        StringBuilder sb = new StringBuilder();
        int size = filterWord.length();
        for (int i = 0; i < size; i++) {
            sb.append(Constants.FILTER_REPLACE);
        }

        return sb.toString();
    }

    public String getHighlight(String filterWord) {
        StringBuilder sb = new StringBuilder();
        return sb.append("<font color=\"red\">")
                .append(filterWord)
                .append("</font>")
                .toString();
    }

    /**
     * Description 根据过滤结果对象FilterResultDto获取敏感词信息
     * @param filterResultDto
     * @param isHighlight
     * @return
     */
    public String getFilterWordInfo(FilterResultDto filterResultDto, boolean isHighlight) {
        if (filterResultDto != null && filterResultDto.getResult().size() > 0) {
            StringBuilder sb = new StringBuilder();
            String color, level;
            int i = 0;
            FilterWordLevel filterWordLevel;
            FilterWord filterWord;
            for (Map.Entry<Integer, List<FilterWordResultDto>> entry : filterResultDto.getResult().entrySet()) {
                if (entry.getValue().size() > 0) {
                    filterWordLevel = FilterWordLevel.getById(entry.getKey());
                    color = filterWordLevel.getColor();
                    level = filterWordLevel.getName();

                    for (FilterWordResultDto o : entry.getValue()) {
                        filterWord = o.getFilterWord();
                        if (i++ > 0) {
                            sb.append(";");
                        }
                        if (isHighlight) {
                            if (filterWord.getReplaceStrategyType() == FilterReplaceStrategyType.None.getId()) {
                                color = "#000000";
                            }
                            sb.append("<font color=\"").append(color).append("\">");
                        }
                        sb.append(filterWord.getWord()).append(",").append(level).append(",").append(o.getCount());
                        if (isHighlight) {
                            sb.append("</font>");
                        }
                    }
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }

        return "";
    }


    public void publish() {

    }
}
