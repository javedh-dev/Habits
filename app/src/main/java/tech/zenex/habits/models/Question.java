/*
 * Copyright (c) 2020.  Zenex.Tech@ https://zenex.tech
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.zenex.habits.models;

import java.util.Map;

public class Question {
    private String question;
    private Map<String, String> options;
    private QuestionType questionType;

    public Question(String question, QuestionType questionType) {
        this.question = question;
        this.options = null;
        this.questionType = questionType;
    }

    public Question(String question, Map<String, String> options, QuestionType questionType) {
        this.question = question;
        this.options = options;
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void addOptions(String option, String value) {
        this.options.put(option, value);
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    private enum QuestionType {BINARY, CHOICE, TEXT}
}
