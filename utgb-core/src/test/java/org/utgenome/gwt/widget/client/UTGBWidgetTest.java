/*--------------------------------------------------------------------------
 *  Copyright 2008 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// utgb-widget Project
//
// UTGBWidgetTest.java
// Since: Apr 24, 2008
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import org.junit.Test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * UTGB Widget Test screen
 * 
 * @author leo
 * 
 */
public class UTGBWidgetTest implements EntryPoint {

	@Test
	public void dummyTest() {

	}

	public void onModuleLoad() {

		UTGBDesignFactory iconFactory = new UTGBDesignFactory();
		HorizontalPanel hp = new HorizontalPanel();

		// CSS.backgroundImage(RootPanel.get(), "theme/default/trackframe-bg.png");

		hp.add(iconFactory.getCloseButton());
		hp.add(iconFactory.getAdjustHightButton());
		hp.add(iconFactory.getConfigButton());
		hp.add(iconFactory.getFixedHightButton());
		hp.add(iconFactory.getOpenButton());
		hp.add(iconFactory.getHideButton());
		hp.add(new Image(iconFactory.getUTGBImageBundle().windowResizeIcon()));

		final NowLoadingIcon loading = iconFactory.getNowLoadingIcon();
		hp.add(loading);

		// Button b = new Button("load");
		loading.addClickHandler(new ClickHandler() {
			boolean toggle = loading.isLoading();

			public void onClick(ClickEvent e) {
				loading.setLoading(toggle = !toggle);
			}
		});
		// vp.add(b);

		// ToggleButton button = new ToggleButton(iconFactory.getUTGBImageBundle().trackReloadIcon().createImage(), new
		// Image("theme/default/trackLoading.gif"));
		// hp.add(button);

		VerticalDraggableTrackPanel vp2 = new VerticalDraggableTrackPanel();

		vp2.add(hp);
		vp2.setSpacing(0);
		vp2.setBorderWidth(0);

		HorizontalTrackFrame hTrackPanel = new HorizontalTrackFrame();
		hTrackPanel.setSize(700, 40);
		vp2.add(hTrackPanel);
		HorizontalTrackFrame hTrackPanel2 = new HorizontalTrackFrame();
		hTrackPanel.setHeight(100);
		hTrackPanel2.setVisible(TrackFrame.BUTTON_CLOSE | TrackFrame.BUTTON_MINIMIZE, false);
		hTrackPanel2.setTrackTitle("horizontal track");
		vp2.add(hTrackPanel2);
		hTrackPanel2.addTrackButtonListener(new TrackButtonListenerAdapter() {
			boolean loading = false;

			@Override
			public void onClickReloadButton(TrackFrame sender) {
				sender.setLoading(loading = !loading);
			}
		});

		TrackWindowPanel wp = new TrackWindowPanel();
		wp.setTrackTitle("window track");
		wp.setSize(300, 100);
		wp.addTrackButtonListener(new TrackButtonListenerAdapter() {
			boolean loading = false;

			@Override
			public void onClickReloadButton(TrackFrame sender) {
				sender.setLoading(loading = !loading);
			}
		});

		VerticalDraggableTrackPanel hp2 = new VerticalDraggableTrackPanel();
		Style.fullWidth(hp2);
		hp2.add(new HorizontalTrackFrame());
		hp2.add(new HorizontalTrackFrame());
		hp2.add(new TrackWindowPanel());
		wp.setTrackContent(hp2);

		vp2.add(wp);

		VerticalPanel l = new VerticalPanel();
		l.add(vp2);

		TabbedTrackFrame tabFrame = new TabbedTrackFrame();

		VerticalDraggableTrackPanel vdp = new VerticalDraggableTrackPanel();
		VerticalDraggableTrackPanel vdp2 = new VerticalDraggableTrackPanel();

		Style.backgroundColor(vdp, "CCCCCC");
		Style.backgroundColor(vdp2, "CCCCFF");

		tabFrame.addTab(vdp, new Tab("Tab 1"));
		tabFrame.addTab(vdp2, new Tab("long tab name hello!!"));

		tabFrame.enableClose(0, false);

		HorizontalTrackFrame h1 = new HorizontalTrackFrame();
		h1.setTrackTitle("T1");
		HorizontalTrackFrame h2 = new HorizontalTrackFrame();
		h2.setTrackTitle("T2");

		// tabFrame.makeDraggable(h1);
		// tabFrame.makeDraggable(h2);

		vdp.add(h1);
		vdp.add(h2);

		tabFrame.selectTab(0);

		l.add(tabFrame);

		HorizontalTrackFrame ht1 = new HorizontalTrackFrame();
		ht1.setTrackContent(new TrackWindowPanel());
		l.add(ht1);

		final GWTCanvas canvas = new GWTCanvas(200, 200);
		canvas.setFillStyle(new Color(200, 0, 0));
		canvas.fillRect(10, 10, 55, 50);
		canvas.setGlobalAlpha(0.5f);
		canvas.setFillStyle(new Color(0, 200, 0));
		canvas.fillRect(30, 30, 55, 50);

		canvas.setGlobalAlpha(1);
		canvas.setLineWidth(1);
		canvas.setStrokeStyle(Color.GREEN);

		canvas.setFillStyle(new Color(255, 0, 0));

		canvas.beginPath();
		canvas.moveTo(30, 30);
		canvas.lineTo(150, 150);
		// was: canvas.quadraticCurveTo(60, 70, 70, 150); which is wrong.
		canvas.cubicCurveTo(60, 70, 60, 70, 70, 150); // <- this is right formula for the image on the right ->
		canvas.lineTo(30, 30);
		canvas.fill();

		canvas.setStrokeStyle(Color.BLACK);
		canvas.beginPath();
		canvas.arc(75, 75, 50, 0, (float) (Math.PI * 2), true); // �O�̉~
		canvas.moveTo(110, 75);
		canvas.arc(75, 75, 35, 0, (float) Math.PI, false); // �� (���v���)
		canvas.moveTo(65, 65);
		canvas.arc(60, 65, 5, 0, (float) (Math.PI * 2), true); // ����
		canvas.moveTo(95, 65);
		canvas.arc(90, 65, 5, 0, (float) (Math.PI * 2), true); // �E��
		canvas.stroke();

		canvas.setLineWidth(1);
		canvas.setStrokeStyle(Color.GREEN);

		canvas.saveContext();

		canvas.setLineWidth(1);
		canvas.setStrokeStyle(Color.RED);

		canvas.translate(100, 100);
		canvas.strokeRect(0, 0, 100, 100);
		canvas.restoreContext();

		canvas.strokeRect(1, 1, 80, 80);

		VerticalPanel vp = new VerticalPanel();
		vp.add(canvas);

		Button b = new Button("scroll");
		b.addClickHandler(new ClickHandler() {
			int x = 10;

			public void onClick(ClickEvent e) {
				canvas.clear();
				canvas.setGlobalAlpha(0.3f);
				canvas.setFillStyle(new Color(0, 255, 255));
				canvas.fillRect(x, 100, 40, 20);
				x += 10;
			}
		});
		vp.add(b);

		vdp.add(vp);

		RootPanel.get().add(vdp);
	}
}
