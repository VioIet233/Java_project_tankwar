package com.tankwar;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameServer {
	private Selector selector;

	private InetSocketAddress listenAddress;
	private final static int PORT = 9093;

	public static void main(String[] args) throws Exception {
		try {
			new GameServer("localhost", 9093).startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GameServer(String address, int port) throws IOException {
		listenAddress = new InetSocketAddress(address, PORT);
	}

	private void startServer() throws IOException {
		this.selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		serverChannel.socket().bind(listenAddress);
		serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		SeverPanel severPanel = new SeverPanel();
		severPanel.start();
		while (true) {

			int readyCount = selector.select();
			if (readyCount == 0) {
				continue;
			}
			severPanel.g.enemySpawn.start();
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator iterator = readyKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				iterator.remove();

				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					this.accept(key);
				} else if (key.isReadable()) {
					this.read(key);
				}
			}
		}
	}

	public class SeverPanel extends Thread {
		GamePanel g = new GamePanel();

		@Override
		public void run() {
			g.setTitle("简易坦克大战-服务器端");
			g.setSize(g.width, g.height);
			g.setLocationRelativeTo(null);
			g.setDefaultCloseOperation(3);
			g.setResizable(false);
			g.setVisible(true);
			g.state = 2;
			g.playerA = new Player("yellow", 500, 660, g);
			g.playerB = new Player("green", 400, 660, g);
			g.playerC = new Player("red", 600, 660, g);
			try {
				g.load("src\\main\\resources\\save\\save0");
			} catch (Exception e) {
				e.getStackTrace();
			}
			g.tankList.add(g.playerA);
			g.tankList.add(g.playerB);
			g.tankList.add(g.playerC);

			while (true) {
				g.repaint();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
				int size = g.bytelList.size();
				if (size != 0){
					broadcast(g.bytelList.get(size - 1));
				}
			}
		}
	}

	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		System.out.println("Connected to: " + remoteAddr);

		channel.register(this.selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);

		if (numRead == -1) {
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return;
		}

		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0, data, 0, numRead);
		System.out.println("Got: " + new String(data));
	}

	private void broadcast(byte[] by) {
		for (SelectionKey key : selector.keys()) {
			if (key.isValid() && key.channel() instanceof SocketChannel) {
				SocketChannel channel = (SocketChannel) key.channel();
				
				System.out.println(by);
				System.out.println(by.length);
				
				send(channel, by);
			}
		}
	}

	private void send(SocketChannel channel, byte[] by) {
		try {
			
			ByteBuffer buffer = ByteBuffer.wrap(by);
			while (buffer.hasRemaining()) {
				channel.write(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}