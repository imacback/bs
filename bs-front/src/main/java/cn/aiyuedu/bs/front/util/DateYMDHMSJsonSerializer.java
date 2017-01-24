package cn.aiyuedu.bs.front.util;

import com.duoqu.commons.utils.DateUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateYMDHMSJsonSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = formatter.format(date);
//        gen.writeString(formattedDate);
        gen.writeString(DateUtils.getDateString(date,"yyyy-MM-dd"));
    }
}