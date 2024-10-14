/*
 * This file is part of "ReleaseChecker", licensed under MIT License.
 *
 *  Copyright (c) 2024 neziw
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package ovh.neziw.checker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.net.ssl.HttpsURLConnection;
import java.net.URI;
import java.net.URL;
import ovh.neziw.checker.release.ReleaseData;
import ovh.neziw.checker.repository.RepositoryData;
import ovh.neziw.checker.util.GsonUtil;

public class ReleaseCheck {

    private static final String BASE_URL = "https://api.github.com/repos/";
    private final String repositoryOwner;
    private final String repositoryName;
    private final String githubToken;
    private RepositoryData repositoryData;
    private ReleaseData releaseData;

    protected ReleaseCheck(final ReleaseCheckBuilder releaseCheckBuilder) {
        this.repositoryOwner = releaseCheckBuilder.getRepositoryOwner();
        this.repositoryName = releaseCheckBuilder.getRepositoryName();
        this.githubToken = releaseCheckBuilder.getGithubToken();
        this.repositoryData = null;
        this.releaseData = null;
    }

    public RepositoryData getRepositoryData() throws IOException {
        if (this.repositoryData != null) {
            return this.repositoryData;
        }

        final URL url = URI.create(BASE_URL + this.repositoryOwner + "/" + this.repositoryName).toURL();
        final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setRequestMethod("GET");
        httpsURLConnection.setRequestProperty("Accept", "application/json");

        if (this.githubToken != null) {
            httpsURLConnection.setRequestProperty("Authorization", "Bearer " + this.githubToken);
        }

        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()))) {
            final StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            this.repositoryData = this.parseRepositoryData(stringBuilder.toString());
        } finally {
            httpsURLConnection.disconnect();
        }
        return this.repositoryData;
    }

    public ReleaseData getLatestRelease() throws IOException {
        if (this.releaseData != null) {
            return this.releaseData;
        }

        final URL url = URI.create(BASE_URL + this.repositoryOwner + "/" + this.repositoryName + "/releases/latest").toURL();
        final HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept", "application/json");

        if (this.githubToken != null) {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + this.githubToken);
        }

        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            final StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            this.releaseData = this.parseReleaseData(stringBuilder.toString());
        } finally {
            httpURLConnection.disconnect();
        }
        return this.releaseData;
    }

    public boolean isNewerVersionAvailable(final String version) throws IOException {
        final String[] parts = version.split("\\.");
        final String[] latestParts = this.parseCleanVersionString(this.getLatestRelease().tagName()).split("\\.");
        for (int i = 0; i < Math.min(parts.length, latestParts.length); i++) {
            final int part = Integer.parseInt(parts[i]);
            final int latestPart = Integer.parseInt(latestParts[i]);

            if (part < latestPart) {
                return true;
            } else if (part > latestPart) {
                return false;
            }
        }
        return parts.length < latestParts.length;
    }

    private RepositoryData parseRepositoryData(final String responseBody) throws IOException {
        final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        return GsonUtil.getGson().fromJson(jsonObject, RepositoryData.class);
    }

    private ReleaseData parseReleaseData(final String responseBody) throws IOException {
        final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        return GsonUtil.getGson().fromJson(jsonObject, ReleaseData.class);
    }

    private String parseCleanVersionString(final String string) {
        return string.replaceAll("[^\\d.]", "");
    }
}
