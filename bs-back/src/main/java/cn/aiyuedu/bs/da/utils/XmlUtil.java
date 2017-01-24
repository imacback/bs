package cn.aiyuedu.bs.da.utils;

import com.google.common.collect.Lists;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.lagarto.dom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by tonydeng on 14-6-10.
 */
public class XmlUtil {
    private static final Logger log = LoggerFactory.getLogger(XmlUtil.class);
    private static Jerry.JerryParser parser = null;
    /**
     * xml序列化到java对象
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T xmlToJavaByJAXB(byte[] data, Class clazz) throws JAXBException {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            return (T) um.unmarshal(new ByteArrayInputStream(data));
        } catch (JAXBException e) {
            if (log.isErrorEnabled())
                log.error("class:'" + clazz.getClass() + "'xml to  java by jaxb error:'" + e.getMessage() + "'");
            throw e;
        }
    }


    public static String xmlChildCDATAToString(String xml,String childNode){
        return getJerryParser().parse(xml).$(childNode).text();
    }

    /**
     * xml子节点序列化到java对象
     * @param xml
     * @param childNode
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T xmlChildNodeToJavaByJaxb(String xml, String childNode, Class clazz) throws JAXBException {
        return xmlToJavaByJAXB(getJerryParser().parse(xml)
                .$(childNode)
                .htmlAll(true)
                .getBytes(), clazz);
    }

    /**
     * xml子节点序列化到java对象列表
     * @param xml
     * @param childNode
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> xmlChildNodesToJavaByJaxb(String xml, String childNode, Class clazz) throws JAXBException {
        Node[] nodes = getJerryParser().parse(xml).$(childNode).get();
        List<T> lists = Lists.newArrayList();
        for(Node node:nodes){
            lists.add((T) xmlToJavaByJAXB(node.getHtml().getBytes(),clazz));
        }
        return lists;
    }

    public static Jerry.JerryParser getJerryParser(){
        if(parser == null){
            parser = new Jerry.JerryParser();
            LagartoDOMBuilder builder = (LagartoDOMBuilder) parser.getDOMBuilder();
            builder.enableXmlMode();
        }
        return parser;
    }
}
