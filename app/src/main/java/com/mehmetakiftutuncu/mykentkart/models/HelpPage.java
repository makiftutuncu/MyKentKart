/*
 * Copyright (C) 2015 Mehmet Akif Tütüncü
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mehmetakiftutuncu.mykentkart.models;

/**
 * A model of a single page in Help screen
 *
 * @author mehmetakiftutuncu
 */
public class HelpPage {
    /** Resource id of title text of the help page */
    public int titleResourceId;
    /** Resource id of image of the help page */
    public int imageResourceId;
    /** Resource id of message text of the help page */
    public int messageResourceId;

    /**
     * Constructor initializing all values
     *
     * @param titleResourceId   Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.HelpPage#titleResourceId}
     * @param imageResourceId   Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.HelpPage#imageResourceId}
     * @param messageResourceId Value to set as {@link com.mehmetakiftutuncu.mykentkart.models.HelpPage#messageResourceId}
     */
    public HelpPage(int titleResourceId, int imageResourceId, int messageResourceId) {
        this.titleResourceId = titleResourceId;
        this.imageResourceId = imageResourceId;
        this.messageResourceId = messageResourceId;
    }
}
