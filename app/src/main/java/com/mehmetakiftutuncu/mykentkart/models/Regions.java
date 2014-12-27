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

import android.content.Context;

import com.mehmetakiftutuncu.mykentkart.R;

import java.util.ArrayList;

public enum Regions {
    ADANA    ("003", R.string.kentKart_region_adana),
    ALANYA   ("019", R.string.kentKart_region_alanya),
    ANTAKYA  ("018", R.string.kentKart_region_antakya),
    BANDIRMA ("008", R.string.kentKart_region_bandırma),
    BURDUR   ("017", R.string.kentKart_region_burdur),
    CANAKKALE("007", R.string.kentKart_region_canakkale),
    EDIRNE   ("013", R.string.kentKart_region_edirne),
    EREGLI   ("020", R.string.kentKart_region_eregli),
    INEGOL   ("015", R.string.kentKart_region_inegol),
    IZMIR    ("006", R.string.kentKart_region_izmir),
    KOCAELI  ("004", R.string.kentKart_region_kocaeli),
    MANISA   ("001", R.string.kentKart_region_manisa),
    MERSIN   ("016", R.string.kentKart_region_mersin),
    MUGLA    ("010", R.string.kentKart_region_mugla),
    NIGDE    ("023", R.string.kentKart_region_nigde),
    SIVAS    ("005", R.string.kentKart_region_sivas);

    public String code;
    public int nameStringId;

    Regions(String code, int nameStringId) {
        this.code = code;
        this.nameStringId = nameStringId;
    }

    public static Regions withCode(String code) {
        for (Regions r : Regions.values()) {
            if (r.code.equals(code)) {
                return r;
            }
        }

        return null;
    }

    public static ArrayList<String> getNames(Context context) {
        ArrayList<String> regions = new ArrayList<String>();

        for (Regions r : Regions.values()) {
            regions.add(context.getString(r.nameStringId));
        }

        return regions;
    }
}
