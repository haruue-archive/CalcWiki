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
        ArrayList<String> stringCookies;
        ArrayList<Cookie> cookies = new ArrayList<Cookie>(0);
        try {
            stringCookies = (ArrayList<String>) folder.readObjectFromFile(getFilenameOfUrl(url));
            if (stringCookies != null) {
                for (String i: stringCookies) {
                    Cookie thisCookie = Cookie.parse(url, i);
                    if (thisCookie.expiresAt() > System.currentTimeMillis()) {
                        cookies.add(thisCookie);
                    }
                }
            } else {
                cookies = new ArrayList<Cookie>(0);
            }
        } catch (Exception e) {
            cookies = new ArrayList<Cookie>(0);
        }
        return cookies;
    }

    private void commitCookiesOfUrl(HttpUrl url, List<String> stringCookies) {
        folder.writeObjectToFile(stringCookies, getFilenameOfUrl(url));
    }

    public void removeCookiesOfUrl(HttpUrl url) {
        initCookieFolder();
        folder.deleteChild(getFilenameOfUrl(url));
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        initCookieFolder();
        ArrayList<String> stringCookies = new ArrayList<String>(0);
        for (Cookie i: cookies) {
            stringCookies.add(i.toString());
        }
        commitCookiesOfUrl(url, stringCookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        initCookieFolder();
        return getCookiesOfUrl(url);
    }
}
