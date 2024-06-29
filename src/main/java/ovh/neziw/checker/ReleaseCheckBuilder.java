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

public class ReleaseCheckBuilder {

    private String repositoryOwner;
    private String repositoryName;
    private String githubToken;

    public static ReleaseCheckBuilder builder() {
        return new ReleaseCheckBuilder();
    }

    public ReleaseCheck build() {
        return new ReleaseCheck(this);
    }

    public String getRepositoryOwner() {
        return this.repositoryOwner;
    }

    public ReleaseCheckBuilder setRepositoryOwner(final String repositoryOwner) {
        this.repositoryOwner = repositoryOwner;
        return this;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public ReleaseCheckBuilder setRepositoryName(final String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public String getGithubToken() {
        return this.githubToken;
    }

    public ReleaseCheckBuilder setGithubToken(final String githubToken) {
        this.githubToken = githubToken;
        return this;
    }
}