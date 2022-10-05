package com.example.engine.util;

import com.example.engine.model.HtmlEntity;
import com.example.engine.services.MainService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;
@Component

public class ForkJoinTest extends RecursiveAction {

    private List<String> listHtmlLinks;
    private String url;
    private volatile List<String> listFixHtmlLinks;
    private volatile List<String> listCode;
    private volatile List<String> listHtml;
//    private final String regexStr = "^(https://matrixvr\\.club/).*";

    private String regexStr;

    private  MainService mainService;


    public ForkJoinTest() {
    }

    public ForkJoinTest(List<String> listHtmlLinks, List<String> listFixHtmlLinks, String url, List<String> listCode, List<String> listHtml, MainService mainService) {
        if (url.contains(".")) {
            regexStr = "^(" + url.replaceAll("\\.", "\\\\\\.") + ").*";
        }
        else
            regexStr = "^(" + url + ").*";
        this.listHtmlLinks = listHtmlLinks;
        this.listFixHtmlLinks = listFixHtmlLinks;
        this.url = url;
        this.listCode = listCode;
        this.listHtml = listHtml;
        this.mainService = mainService;
    }

    @Override
    protected void compute() {
        List<ForkJoinTest> forkJoinTests = new ArrayList<>();

        if (listHtmlLinks.contains(url)) {
            return;
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        listHtmlLinks.add(url);

        try {
            Connection.Response response = Jsoup.connect(url).userAgent("SearchBot").execute();
            Elements elements1 = response.parse().getAllElements();
            Elements elements = elements1.select("a");
            for (int i = 0; i < elements.size(); i++) {
                String str = elements.get(i).absUrl("href");
                if (Pattern.matches(regexStr, str) && !str.contains("#") && !listHtmlLinks.contains(str)) {
                    Connection.Response response1 = Jsoup.connect(str).userAgent("SearchBot").execute();
                    String url1 = elements.get(i).absUrl("href");
                    listHtmlLinks.add(url1);
                    synchronized (this) {
                        HtmlEntity htmlEntity = new HtmlEntity();
                        if (isPullPath(url1)) {
                            htmlEntity.setPath(pullPath(url1));
                            listFixHtmlLinks.add(pullPath(url1));
                        }
                        htmlEntity.setCode(Integer.parseInt(String.valueOf(response.statusCode())));
                        listCode.add(String.valueOf(response.statusCode()));
                        htmlEntity.setContent(response1.parse().html());
                        listHtml.add(elements1.get(i).html());
                        mainService.save(htmlEntity);
                    }
                    ForkJoinTest forkJoinTest = new ForkJoinTest(listHtmlLinks, listFixHtmlLinks, str, listCode, listHtml, mainService);
                    forkJoinTest.fork();
                    forkJoinTests.add(forkJoinTest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<List<String>> links = new ArrayList<>();
        for (ForkJoinTest forkJoinTest : forkJoinTests) {
            forkJoinTest.join();
        }
    }

    public String pullPath(String url) throws IOException {
        String fixUrl = url;
        String url2 = this.url.substring(0, this.url.length() - 1);
        if (fixUrl.replaceAll(url2, "/").equals("/")) {
            return "/";
        }
        fixUrl = fixUrl.replaceAll(this.url, "");
        if (!listFixHtmlLinks.contains(fixUrl)) {
            if (!fixUrl.equals("/")) {
                if (Pattern.matches("^.+[/].+", fixUrl)) {
                    fixUrl = fixUrl.replaceAll(".+[/]", "/");
                }
                fixUrl = "/" + fixUrl.replaceAll("/", "") + ".html";
            }
        }
        return fixUrl;
    }

    public boolean isPullPath(String url) throws IOException {
        String fixUrl = url;
        String url2 = this.url.substring(0, this.url.length() - 1);
        System.out.println(url + " - " + url2);
        if (fixUrl.replaceAll(url2, "/").equals("/")) {
            return !listFixHtmlLinks.contains("/");
        }
        fixUrl = "/" + fixUrl.replaceAll(this.url, "");
        if (Pattern.matches("^.+[/].+", fixUrl)) {
            fixUrl = fixUrl.replaceAll(".+[/]", "/");
        }
        return !listFixHtmlLinks.contains(fixUrl);
    }
}