package Dictionary;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslatorAPI extends Task<String> {
    private final String langFrom;
    private final String langTo;
    private final String text;

    public TranslatorAPI(String langFrom, String langTo, String text) {
        this.langFrom = langFrom;
        this.langTo = langTo;
        this.text = text;
    }

    public String translate() throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycby3AOWmhe32TgV9nW-Q0TyGOEyCHQeFiIn7hRgy5m8jHPaXDl2GdToyW_3Ys5MTbK6wjg/exec"
                + "?q="
                + URLEncoder.encode(text, StandardCharsets.UTF_8)
                + "&target="
                + langTo
                + "&source="
                + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    @Override
    protected String call() throws Exception {
        return translate();
    }
}
