package core;

import world.QuadScene;

import com.metaplains.core.GameClient;

import gui.GUIQuadrocopter;

public class SerialDataHandler implements SerialEventListener {

	private QuadClient quadClient;
	private String msgBuffer;
	private CSVWriter csvWriter;

	public SerialDataHandler(QuadClient quadClient) {
		this.quadClient = quadClient;
		new Thread(new Runnable() {
			@Override
			public void run() {
				new SerialCom(SerialDataHandler.this).initialize();
				System.out.println("Serial Connection Established.");
			}
		}, "SerialListener Thread").start();
		csvWriter = new CSVWriter("test");
	}
	
	private void onNewLine(String line) {
		//System.out.print(line);
		if (quadClient.currentGUIScreen != null && quadClient.currentGUIScreen instanceof GUIQuadrocopter) {
			if (line.startsWith("B:")) {
				/* Button pressed. */
				((GUIQuadrocopter) quadClient.currentGUIScreen).onButtonDown();
			} else {
				csvWriter.write(line+"\n");
				String[] nums = line.split(",");
				if (nums.length > 3) {
					try {
						int accX = Integer.parseInt(nums[0]), accY = Integer.parseInt(nums[1]), accZ = Integer.parseInt(nums[2]);
						float angle = Float.parseFloat(nums[3]);
						((GUIQuadrocopter) quadClient.currentGUIScreen).setParameters(accX, accY, accZ, angle);
						if (GameClient.game.currentWorld instanceof QuadScene) {
							((QuadScene)GameClient.game.currentWorld).quad.z = 2-accZ/100F;
							((QuadScene)GameClient.game.currentWorld).quad.yaw = -angle;
						}
					} catch (NumberFormatException e) {
						//System.err.println(e.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void onSerialEvent(String msg) {
		msgBuffer += msg;
		if (msgBuffer.contains("\n")) {
			String[] msgSplit = msgBuffer.split("\n");
			if (msgSplit.length > 1) {
				onNewLine(msgSplit[0]);
				msgBuffer = msgSplit[1];
			}
		}
	}
}