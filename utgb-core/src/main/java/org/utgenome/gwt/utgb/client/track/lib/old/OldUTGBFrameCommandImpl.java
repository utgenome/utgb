/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// GenomeBrowser Project
//
// OldUTGBFrameOperationImpl.java
// Since: 2007/06/21
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track.lib.old;

import org.utgenome.gwt.utgb.client.track.Track;
import org.utgenome.gwt.utgb.client.track.TrackGroup;
import org.utgenome.gwt.utgb.client.track.TrackGroupPropertyWriter;
import org.utgenome.gwt.utgb.client.track.TrackWindow;
import org.utgenome.gwt.utgb.client.track.operation.FrameCommand;

import com.google.gwt.xml.client.Node;

public class OldUTGBFrameCommandImpl implements FrameCommand {

	public static FrameCommand newInstance(final Node frameCommandNode) {
		final String name = Utilities.getAttributeValue(frameCommandNode, "name");

		if (name.equals("moverel")) {
			final int value = Integer.parseInt(Utilities.getAttributeValue(frameCommandNode, "value", "0"));

			return new FrameMoveRelCommand(value);
		}
		else if (name.equals("moveabs")) {
			final int value = Integer.parseInt(Utilities.getAttributeValue(frameCommandNode, "value", "0"));

			return new FrameMoveAbsCommand(value);
		}
		else if (name.equals("zoomrel")) {
			throw new UnsupportedOperationException("zoomrel");
		}
		else if (name.equals("zoomabs")) {
			final int value = Integer.parseInt(Utilities.getAttributeValue(frameCommandNode, "value", "10000"));

			return new FrameZoomAbsCommand(value);
		}
		else if (name.equals("rev")) {
			return new FrameRevCommand();
		}
		else if (name.equals("setspecies")) {
			final String value = Utilities.getAttributeValue(frameCommandNode, "value");

			if (value != null)
				return new FrameSetSpeciesCommand(value);
		}
		else if (name.equals("setrevision")) {
			final String value = Utilities.getAttributeValue(frameCommandNode, "value");

			if (value != null)
				return new FrameSetRevisionCommand(value);
		}
		else if (name.equals("settarget")) {
			final String value = Utilities.getAttributeValue(frameCommandNode, "value");

			if (value != null)
				return new FrameSetTargetCommand(value);
		}
		else if (name.equals("setwidth")) {
			throw new UnsupportedOperationException("setwidth");
		}
		else if (name.equals("setstart")) {
			final int value = Integer.parseInt(Utilities.getAttributeValue(frameCommandNode, "value", "1"));

			return new FrameSetStartCommand(value);
		}
		else if (name.equals("setend")) {
			final int value = Integer.parseInt(Utilities.getAttributeValue(frameCommandNode, "value", "1"));

			return new FrameSetEndCommand(value);
		}

		throw new UnsupportedOperationException(name);
	}

	public void execute(Track track) {
	}

	public static class FrameMoveRelCommand implements FrameCommand {
		private final int step;

		public FrameMoveRelCommand(final int step) {
			this.step = step;
		}

		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackWindow trackWindow = trackGroup.getTrackWindow();

			final int newStartIndex = trackWindow.getStartOnGenome() + step;
			final int newEndIndex = trackWindow.getEndOnGenome() + step;

			trackGroup.setTrackWindowLocation(newStartIndex, newEndIndex);
		}
	}

	public static class FrameMoveAbsCommand implements FrameCommand {
		private final int centerIndex;

		public FrameMoveAbsCommand(final int centerIndex) {
			this.centerIndex = centerIndex;
		}

		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackWindow trackWindow = trackGroup.getTrackWindow();

			final int oldStartIndex = trackWindow.getStartOnGenome();
			final int oldEndIndex = trackWindow.getEndOnGenome();

			final int oldCenterIndex = (oldStartIndex + oldEndIndex) / 2;

			final int step = centerIndex - oldCenterIndex;

			final int newStartIndex = oldStartIndex + step;
			final int newEndIndex = oldEndIndex + step;

			trackGroup.setTrackWindowLocation(newStartIndex, newEndIndex);
		}
	}

	public static class FrameZoomAbsCommand implements FrameCommand {
		private final int width;

		public FrameZoomAbsCommand(final int width) {
			this.width = width;
		}

		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackWindow trackWindow = trackGroup.getTrackWindow();

			final int oldStartIndex = trackWindow.getStartOnGenome();
			final int oldEndIndex = trackWindow.getEndOnGenome();

			final int oldCenterIndex = (oldStartIndex + oldEndIndex) / 2;

			final int step = width / 2;

			final int _diff = oldEndIndex - oldStartIndex;
			final boolean isPlus = _diff >= 0;

			final int coeff = isPlus ? +1 : -1;

			final int newStartIndex = oldCenterIndex - coeff * step;
			final int newEndIndex = oldCenterIndex + coeff * step;

			trackGroup.setTrackWindowLocation(newStartIndex, newEndIndex);
		}
	}

	public static class FrameRevCommand implements FrameCommand {
		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackWindow trackWindow = trackGroup.getTrackWindow();

			final int oldStartIndex = trackWindow.getStartOnGenome();
			final int oldEndIndex = trackWindow.getEndOnGenome();

			trackGroup.setTrackWindowLocation(oldEndIndex, oldStartIndex);
		}
	}

	public static class FrameParameterSetCommand implements FrameCommand {
		protected final String key;
		protected final String value;

		public FrameParameterSetCommand(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackGroupPropertyWriter propertyWriter = trackGroup.getPropertyWriter();

			propertyWriter.setProperty(key, value);
		}
	}

	public static class FrameSetSpeciesCommand extends FrameParameterSetCommand {
		public FrameSetSpeciesCommand(final String newSpecies) {
			super(OldUTGBProperty.SPECIES, newSpecies);
		}
	}

	public static class FrameSetRevisionCommand extends FrameParameterSetCommand {
		public FrameSetRevisionCommand(final String newRevision) {
			super(OldUTGBProperty.REVISION, newRevision);
		}
	}

	public static class FrameSetTargetCommand extends FrameParameterSetCommand {
		public FrameSetTargetCommand(final String newTarget) {
			super(OldUTGBProperty.TARGET, newTarget);
		}
	}

	public static class FrameSetStartCommand implements FrameCommand {
		private final int newStartIndex;

		public FrameSetStartCommand(final int newStartIndex) {
			this.newStartIndex = newStartIndex;
		}

		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackWindow trackWindow = trackGroup.getTrackWindow();

			trackGroup.setTrackWindowLocation(newStartIndex, trackWindow.getEndOnGenome());
		}
	}

	public static class FrameSetEndCommand implements FrameCommand {
		private final int newEndIndex;

		public FrameSetEndCommand(final int newEndIndex) {
			this.newEndIndex = newEndIndex;
		}

		public void execute(final Track track) {
			final TrackGroup trackGroup = track.getTrackGroup();

			final TrackWindow trackWindow = trackGroup.getTrackWindow();

			trackGroup.setTrackWindowLocation(trackWindow.getStartOnGenome(), newEndIndex);
		}
	}
}
