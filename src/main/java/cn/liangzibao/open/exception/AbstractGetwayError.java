// Copyright 2017 Liangzibao, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// See the License for the specific language governing permissions and
// limitations under the License.

package cn.liangzibao.open.exception;

/**
 * Created by wangbai on 2017/2/21.
 */
public abstract class AbstractGetwayError extends Exception {

    public AbstractGetwayError() {
        super();
    }

    public AbstractGetwayError(String message) {
        super(message);
    }

    public AbstractGetwayError(String message, Throwable cause) {
        super(message, cause);
    }

}
