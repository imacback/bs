package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.BaseTest;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.FilterType;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.dto.FilterWordResultDto;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.service.FilterWordGeneralService;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class FilterWordServiceTest extends BaseTest {

    @Autowired
    private FilterWordService filterWordService;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;

    @Test
    public void testMultiAdd() {
        Integer levelId = 3;
        String fileName = "s.txt";
        String path = "/project/workspace/rs/rs-back/src/main/webapp/WEB-INF/backup/filterWord/";

        try {
            List<String> readLines = Files.readLines(new File(path+fileName), Charsets.UTF_8);
            List<String> list = null;
            FilterWord f = null;
            Date d = new Date();
            for (String line : readLines) {
                list = Splitter.on("|").omitEmptyStrings().trimResults().splitToList(line);
                for (String s : list) {
                    f = new FilterWord();
                    f.setLevel(levelId);
                    f.setWord(s);
                    f.setCreateDate(d);
                    f.setCreatorId(1);
                    f.setEditDate(d);
                    f.setEditorId(1);

                    filterWordService.save(f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilter() {
        String text = "没多久，上床盖板下的“哔啵”裂响声渐渐小了，取而代之的是阅读币,一种无数鱼儿在水里拍打发出的声音，或者说，9a色色,像是无数泥鳅装在一个小桶里相互钻挤发出的声音, 色色。虽然盖板盖着，但是阅读币从盖板的边缘还是能看到，下面的黑色蛹壳已经看不到了，变成了许多白皙如玉的小肉虫，它们刚刚从沉睡中苏醒，仿佛受到卓木强巴血液的吸引，争先恐后向池子的下层钻去，你推我搡，谁也不让谁。";
        long start = System.currentTimeMillis();
        FilterResultDto r = filterWordGeneralService.filter(text, FilterType.Highlight);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        if (r != null) {
            if (r.getResult() != null && r.getResult().size() > 0) {
                for (Map.Entry<Integer, List<FilterWordResultDto>> entry : r.getResult().entrySet()) {
                    System.out.println(entry.getKey() + "----");
                    for (FilterWordResultDto o : entry.getValue()) {
                        System.out.println(o.getFilterWord().getWord() + ":" + o.getCount());
                    }
                }
            }
            if (StringUtils.isNotEmpty(r.getText())) {
                System.out.println(r.getText());
            }
        }

        System.out.println("******");
        System.out.println(filterWordService.getFilterWordInfo(r));
    }

    public static void main(String[] args) {
        String text = "没多久，盖板下的“哔啵”裂响声渐渐小了，取而代之的是阅读币,一种无数鱼儿在水里拍打发出的声音，或者说，9a色色,像是无数泥鳅装在一个小桶里相互钻挤发出的声音。虽然盖板盖着，但是从盖板的边缘还是能看到，下面的黑色蛹壳已经看不到了，变成了许多白皙如玉的小肉虫，它们刚刚从沉睡中苏醒，仿佛受到卓木强巴血液的吸引，争先恐后向池子的下层钻去，你推我搡，谁也不让谁。";
        String s = "阅读币";
        System.out.println(StringUtils.countMatches(text, s));
    }

    @Test
    public void test() {
        String text = "勾引，内衣，欲望....";

        FilterResultDto r = filterWordGeneralService.filter(text, Constants.FilterType.Replace);

        System.out.println(r.getOriginText());
        System.out.println(r.getText());
    }

}
