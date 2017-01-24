package cn.aiyuedu.bs.da.worker;

import cn.aiyuedu.bs.da.model.CpBook;
import cn.aiyuedu.bs.da.model.CpChapter;
import com.duoqu.commons.urlplus.URL;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by tonydeng on 15/7/31.
 */
public abstract class AbstractWorker {
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    public abstract List<Integer> getCpBookIds();

    public abstract CpBook getBookInfo(String cpBookId);

    public abstract List<CpChapter> getChapterList(String cpBookId);

    /**
     * 组装url
     * @param host
     * @param path
     * @param params
     * @return
     */
    protected URL getURL(@NotNull String host, String path, Map<String,String> params) {
        URL url = new URL(host);
        if(StringUtils.isNotEmpty(path)){
            url.setPath(path);
        }
        if(MapUtils.isNotEmpty(params)){
            for(String key: params.keySet()){
                url.addParameter(key,params.get(key));
            }
        }
        return url;
    }
}
