package cn.lsz.gongzhonghao.hajimiemasidie.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * description
 * 
 * @author LSZ 2020/02/12 17:16
 * @contact 648748030@qq.com
 */
public class XmlBeanUtils {

    public static Object transform(String xml, Class clazz) {
        XStream xstream = XmlBeanUtils.newXStream();
        //目前项目只有两层继承，所以暂时只做两层
        Class superclass = clazz.getSuperclass();
        if (superclass != null){
            xstream.addDefaultImplementation(clazz, superclass);
        }
        xstream.processAnnotations(clazz);
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        List nodes = new ArrayList();
        nodes.add(doc.getRootElement());
        for (Iterator it = nodes.iterator(); it.hasNext();){
            Element elm = (Element) it.next();
            Object dto = xstream.fromXML(elm.asXML());
            return dto;
        }
        return null;
    }

    public static String toXml(Object object){
        XStream xs = new XStream();
        xs.processAnnotations(object.getClass());
        String xml = xs.toXML(object);
        return StringEscapeUtils.unescapeXml(xml);
    }

    private static XStream newXStream(){
        return new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            try {
                                return this.realClass(fieldName) != null;
                            } catch(Exception e) {
                                return false;
                            }
                        } else {
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    }
                };
            }
        };
    }

}
