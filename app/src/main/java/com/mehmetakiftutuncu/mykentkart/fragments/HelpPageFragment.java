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
package com.mehmetakiftutuncu.mykentkart.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mehmetakiftutuncu.mykentkart.R;
import com.mehmetakiftutuncu.mykentkart.models.HelpPage;

public class HelpPageFragment extends Fragment {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_MESSAGE = "message";

    private HelpPage helpPage;

    public static HelpPageFragment with(int titleResourceId, int imageResourceId, int messageResourceId) {
        HelpPageFragment fragment = new HelpPageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_TITLE, titleResourceId);
        arguments.putInt(EXTRA_IMAGE, imageResourceId);
        arguments.putInt(EXTRA_MESSAGE, messageResourceId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            int titleResourceId = arguments.getInt(EXTRA_TITLE, -1);
            int imageResourceId = arguments.getInt(EXTRA_IMAGE, -1);
            int messageResourceId = arguments.getInt(EXTRA_MESSAGE, -1);

            helpPage = new HelpPage(titleResourceId, imageResourceId, messageResourceId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.help_page, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.textView_helpPage_title);
        ImageView image = (ImageView) rootView.findViewById(R.id.imageView_helpPage_image);
        TextView message = (TextView) rootView.findViewById(R.id.textView_helpPage_message);

        if (helpPage != null) {
            if (helpPage.titleResourceId != -1) {
                title.setText(helpPage.titleResourceId);
            }

            if (helpPage.imageResourceId != -1) {
                image.setImageResource(helpPage.imageResourceId);
            }

            if (helpPage.messageResourceId != -1) {
                message.setText(helpPage.messageResourceId);
            }
        }

        return rootView;
    }
}
