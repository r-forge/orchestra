package com.addictedtor.jedit.tools;

import org.gjt.sp.jedit.ServiceManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RServiceManager extends ServiceManager {

    public static boolean IS_DEBUG = false;
    
    private static Map<String, String> map = new HashMap<String, String>();

    public static void init(File file) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("SERVICE");
            System.out.println("Information of all services");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element el = (Element) fstNode;
                    String clazz = el.getAttribute("CLASS");
                    String name = el.getAttribute("NAME");
                    String serviceCmd = el.getFirstChild().getNodeValue();
                    System.out.println(clazz + " : " + name + " : " + serviceCmd);
                    map.put(clazz + "#" + name, serviceCmd);
//                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("Class");
//                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
//                    NodeList fstNm = fstNmElmnt.getChildNodes();
//                    System.out.println("First Name : " + ((Node) fstNm.item(0)).getNodeValue());
//                    NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
//                    Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
//                    NodeList lstNm = lstNmElmnt.getChildNodes();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static java.lang.Object getService(java.lang.String clazz, java.lang.String name) {
        if (IS_DEBUG) {
            try {
                String serviceCmd = map.get(clazz + "#" + name);
                if (serviceCmd == null)
                    return null;
                System.out.println(serviceCmd);
                String s1 = serviceCmd.split("new")[1].trim();
                int i = s1.indexOf("()");
                @SuppressWarnings("unused")
				StringTokenizer st = new StringTokenizer(serviceCmd, "new ");
                String className = s1.substring(0, i).trim();
                System.out.println(className);
                Class<?> cls = Class.forName(className);
                return cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            return ServiceManager.getService(clazz, name);
        }
        return null;
    }
}