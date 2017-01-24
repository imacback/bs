package cn.aiyuedu.bs.common.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CDataAdapter extends XmlAdapter<String, String> {
    public static void main(String[] args) {

        System.out.println(JAXBContext.JAXB_CONTEXT_FACTORY);
    }

    @Override
    public String marshal(String str) throws Exception {

        return "<![CDATA[" + str + "]]>";
    }

    @Override
    public String unmarshal(String str) throws Exception {

        return str;
    }
}
