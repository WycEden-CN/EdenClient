/*
 * Created on Jun 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package cn.wyceden.libraries.flac;

import java.util.HashSet;
import java.util.Iterator;

import cn.wyceden.libraries.flac.metadata.StreamInfo;
import cn.wyceden.libraries.flac.util.ByteData;

/**
 * Class to handle PCM processors.
 * 
 * @author kc7bfi
 */
class PCMProcessors implements PCMProcessor {
	private HashSet<PCMProcessor> pcmProcessors = new HashSet<PCMProcessor>();

	/**
	 * Add a PCM processor.
	 * 
	 * @param processor The processor listener to add
	 */
	public void addPCMProcessor(PCMProcessor processor) {
		synchronized (pcmProcessors) {
			pcmProcessors.add(processor);
		}
	}

	/**
	 * Remove a PCM processor.
	 * 
	 * @param processor The processor listener to remove
	 */
	public void removePCMProcessor(PCMProcessor processor) {
		synchronized (pcmProcessors) {
			pcmProcessors.remove(processor);
		}
	}

	/**
	 * Process the StreamInfo block.
	 * 
	 * @param info the StreamInfo block
	 * @see cn.wyceden.libraries.flac.PCMProcessor#processStreamInfo(cn.wyceden.libraries.flac.metadata.StreamInfo)
	 */
	public void processStreamInfo(StreamInfo info) {
		synchronized (pcmProcessors) {
			Iterator<PCMProcessor> it = pcmProcessors.iterator();
			while (it.hasNext()) {
				PCMProcessor processor = (PCMProcessor) it.next();
				processor.processStreamInfo(info);
			}
		}
	}

	/**
	 * Process the decoded PCM bytes.
	 * 
	 * @param pcm The decoded PCM data
	 * @see cn.wyceden.libraries.flac.PCMProcessor#processPCM(cn.wyceden.libraries.flac.util.ByteData)
	 */
	public void processPCM(ByteData pcm) {
		synchronized (pcmProcessors) {
			Iterator<PCMProcessor> it = pcmProcessors.iterator();
			while (it.hasNext()) {
				PCMProcessor processor = (PCMProcessor) it.next();
				processor.processPCM(pcm);
			}
		}
	}

	public boolean isCanceled() {
		return pcmProcessors.size() == 0;
	}

}
