package com.xiaojumao.bean;

import java.util.List;

public class ShowAPI {
    private String showapi_res_error;
    private String showapi_res_id;
    private Integer showapi_res_code;
    private Body showapi_res_body;

    public ShowAPI() {
    }

    public ShowAPI(String showapi_res_error, String showapi_res_id, Integer showapi_res_code, Body showapi_res_body) {
        this.showapi_res_error = showapi_res_error;
        this.showapi_res_id = showapi_res_id;
        this.showapi_res_code = showapi_res_code;
        this.showapi_res_body = showapi_res_body;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public String getShowapi_res_id() {
        return showapi_res_id;
    }

    public void setShowapi_res_id(String showapi_res_id) {
        this.showapi_res_id = showapi_res_id;
    }

    public Integer getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(Integer showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public Body getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(Body showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public class Body {
        private Integer ret_code;
        private String ret_message;
        private List<Con> data;

        public Body() {
        }

        public Body(Integer ret_code, String ret_message, List<Con> data) {
            this.ret_code = ret_code;
            this.ret_message = ret_message;
            this.data = data;
        }

        public Integer getRet_code() {
            return ret_code;
        }

        public void setRet_code(Integer ret_code) {
            this.ret_code = ret_code;
        }

        public String getRet_message() {
            return ret_message;
        }

        public void setRet_message(String ret_message) {
            this.ret_message = ret_message;
        }

        public List<Con> getData() {
            return data;
        }

        public void setData(List<Con> data) {
            this.data = data;
        }
    }

    public class Con {
        private String english;
        private String chinese;

        public Con() {
        }

        public Con(String english, String chinese) {
            this.english = english;
            this.chinese = chinese;
        }

        public String getEnglish() {
            return english;
        }

        public void setEnglish(String english) {
            this.english = english;
        }

        public String getChinese() {
            return chinese;
        }

        public void setChinese(String chinese) {
            this.chinese = chinese;
        }
    }
}




