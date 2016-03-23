package org.calcwiki.data.network.cookie;

import com.jude.utils.JFileManager;

import org.calcwiki.App;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 持久化 Cookie 文件存储，基于 JFileManager
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class PersistentCookieJar implements CookieJar {

    JFileManager.Folder folder;

    private void initCookieFolder() {
        if (folder == null) {
            folder = JFileManager.getInstance().getFolder(App.ObjectStorageDirs.Cookie);
        }
    }

    private String getFilenameOfUrl(HttpUrl url) {
        // 使用 域名 作为 Url 区分标识
        return url.host();
    }

    private ArrayList<Cookie> getCookiesOfUrl(HttpUrl url) {
        ArrayList<Cookie> cookies;
        try {
            cookies = (ArrayList<Cookie>) folder.readObjectFromFile(getFilenameOfUrl(url));
            if (cookies != null) {
                for (Cookie i: cookies) {
                    if (i.expiresAt() < System.currentTimeMillis()) {
                        cookies.remove(i);
                    }
                }
                return cookies;
            } else {
                return new ArrayList<Cookie>(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Cookie>(0);
        }
    }

    private void commitCookiesOfUrl(HttpUrl url, List<Cookie> cookies) {
        folder.writeObjectToFile(cookies, getFilenameOfUrl(url));
    }


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        initCookieFolder();
        commitCookiesOfUrl(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        initCookieFolder();
        return getCookiesOfUrl(url);
    }
}
