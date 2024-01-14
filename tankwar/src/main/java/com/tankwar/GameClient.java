package com.tankwar;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GameClient {
	String threadName;

	KeyMonitor monitor;
	SocketChannel client;
	byte[] by;
	private final Lock sendLock = new ReentrantLock();

	public void launch() {
		JFrame panel = new JFrame() {
			@Override
			public void paint(Graphics g) {
				try {
					if (by != null) {
						//System.out.println(by);

						ByteArrayInputStream bis = new ByteArrayInputStream(by);
						BufferedImage image = ImageIO.read(bis);
						g.drawImage(image, 0, 0, null);
					}
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		};
		monitor = new KeyMonitor();
		panel.setTitle("简易坦克大战-" + threadName);
		panel.setSize(1000, 720);
		panel.setLocationRelativeTo(null);
		panel.setDefaultCloseOperation(3);
		panel.setResizable(false);

		panel.setVisible(true);
		panel.addKeyListener(monitor);
		while (true) {
			try {
				Thread.sleep(25);
			} catch (Exception e) {
				e.getStackTrace();
			}
			panel.repaint();

		}
	}

	public static byte[] charToByte(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) ((c & 0xFF00) >> 8);
		b[1] = (byte) (c & 0xFF);
		return b;
	}

	public class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			char key = e.getKeyChar();
			send((charToByte(key)));
		}
	}

	public void startClient() throws IOException, InterruptedException {

		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9093);
		client = SocketChannel.open(hostAddress);

		threadName = Thread.currentThread().getName();

		Thread receiveThread = new Thread(this::receive);
		receiveThread.start();
		launch();

	}

	public void send(byte[] data) {
		try {
			sendLock.lock();
			ByteBuffer buffer = ByteBuffer.wrap(data);
			while (buffer.hasRemaining()) {
				client.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			sendLock.unlock();
		}
	}

	private void receive() {
		try {
			while (true) {
				ByteBuffer buffer = ByteBuffer.allocate(204780);
				int bytesRead = client.read(buffer);
				if (bytesRead > 0) {
					buffer.flip();
					by = new byte[buffer.remaining()];
					
					System.out.println(by);
					System.out.println(by.length);
				}
				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Runnable client = new Runnable() {
			@Override
			public void run() {
				try {
					GameClient Gclient = new GameClient();
					Gclient.startClient();
					Gclient.launch();

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};
		new Thread(client, "playerA").start();
		new Thread(client, "playerB").start();
		new Thread(client, "playerC").start();
	}

}