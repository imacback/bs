package cn.aiyuedu.bs.dao.entity;

import java.io.Serializable;

import com.duoqu.commons.web.spring.SequenceGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection="sequence")
public class Sequence implements Serializable {

    private static final long serialVersionUID = -3918262569014434302L;

    public static final Integer STEP = 1;  				// 增幅
    public static final String KEY = "sequence"; 		// 自增key

    @Id
    @SequenceGenerator
    private String id;  								// 主键ID，collection名称
    @Field(value="sequence")
    private Integer sequence;   						// 序列值

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
