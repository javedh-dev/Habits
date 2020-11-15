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

package tech.zenex.habits.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.marcoscg.easyabout.EasyAboutFragment;
import com.marcoscg.easyabout.helpers.AboutItemBuilder;
import com.marcoscg.easyabout.items.AboutCard;
import com.marcoscg.easyabout.items.NormalAboutItem;

import tech.zenex.habits.R;

public class AboutFragment extends EasyAboutFragment {

    @Override
    protected void configureFragment(final Context context, View rootView, Bundle savedInstanceState) {
        addCard(new AboutCard.Builder(context)
                .addItem(AboutItemBuilder.generateAppTitleItem(context)
                        .setSubtitle("by @MarcosCGdev."))
                .addItem(AboutItemBuilder.generateAppVersionItem(context, true)
                        .setIcon(R.mipmap.ic_launcher))
                .addItem(new NormalAboutItem.Builder(context)
                        .setTitle("Licenses")
                        .setIcon(R.drawable.settings)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .build())
                .build());

        addCard(new AboutCard.Builder(context)
                .setTitle("Support")
                .addItem(AboutItemBuilder.generatePlayStoreItem(context)
                        .setTitle("Rate application")
                        .setIcon(R.drawable.info))
                .addItem(AboutItemBuilder.generateLinkItem(context, "https://github" +
                        ".com/marcoscgdev/EasyAbout/issues/new")
                        .setTitle("Report bugs")
                        .setIcon(R.drawable.info))
                .addItem(new NormalAboutItem.Builder(context)
                        .setTitle("Clickable item")
                        .setSubtitle("This item has onClick and onLongClick listener.")
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                    Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
//                                    Toast.makeText(context, "onLongClick", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        })
                        .setIcon(R.drawable.info)
                        .build())
                .build());
    }
}
