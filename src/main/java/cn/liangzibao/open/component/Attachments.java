package cn.liangzibao.open.component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbai on 2017/5/19.
 */
public class Attachments {

    private Map<String, ArrayList<File>> attachmentList = null;

    public Attachments() {
        this.attachmentList = new HashMap<>();
    }

    public void addAttachment(String name, String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return;
        }

        ArrayList<File> fileList =  attachmentList.get(name);
        if (fileList == null) {
            fileList = new ArrayList<File>();
            this.attachmentList.put(name, fileList);
        }
        fileList.add(file);
    }

    public ArrayList<File> getAttachment(String name) {
        return this.attachmentList.get(name);
    }

    public Map<String, ArrayList<File>> getAttachmentList() {
        return this.attachmentList;
    }

    public boolean isEmpty() {
        return this.attachmentList.isEmpty();
    }
    
}
