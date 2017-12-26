package me.chanjar.weixin.mp.util.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

public class XMLUtil {

    /**

     * 将xml转换为Map

     * @param xml

     * @return

     * @throws Exception

     */

    public static Map<String, Object> xml2Map(String xml) throws Exception {

        return xmlDoc2Map(DocumentHelper.parseText(xml));

    }

    /**

     * 将xml文件转成Map

     * @param xmlDoc

     * @return

     */

    public static Map<String, Object> xmlDoc2Map(Document xmlDoc) {

        Map<String, Object> map = new HashMap<String, Object>();

        if (xmlDoc == null) {

            return map;

        }

        Element root = xmlDoc.getRootElement();

        for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {

            Element e = (Element) iterator.next();

            List list = e.elements();

            if (list.size() > 0) {

                map.put(e.getName(), Dom2Map(e,map));

            } else {

                map.put(e.getName(), e.getText());

            }

        }

        return map;

    }

    private static Map Dom2Map(Element e,Map map){

        List list = e.elements();

        if(list.size() > 0){

            for (int i = 0;i < list.size(); i++) {

                Element iter = (Element) list.get(i);

                List mapList = new ArrayList();

                if (iter.elements().size() > 0) {

                    Map m = Dom2Map(iter,map);

                    if (map.get(iter.getName()) != null) {

                        Object obj = map.get(iter.getName());

                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {

                            mapList = new ArrayList();

                            mapList.add(obj);

                            mapList.add(m);

                        }

                        if (obj.getClass().getName().equals("java.util.ArrayList")) {

                            mapList = (List) obj;

                            mapList.add(m);

                        }

                        map.put(iter.getName(), mapList);

                    } else {

                        map.putAll(m);

                    }

                } else {

                    if (map.get(iter.getName()) != null) {

                        Object obj = map.get(iter.getName());

                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {

                            mapList = new ArrayList();

                            mapList.add(obj);

                            mapList.add(iter.getText());

                        }

                        if (obj.getClass().getName().equals("java.util.ArrayList")) {

                            mapList = (List) obj;

                            mapList.add(iter.getText());

                        }

                        map.put(iter.getName(), mapList);

                    } else {

                        map.put(iter.getName(), iter.getText());

                    }

                }

            }

        } else {

            map.put(e.getName(), e.getText());

        }

        return map;

    }

}
