package com.graphql.java.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class StocktickerWebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private StockTickerWebSocketHandler stockTickerWebSocketHandler;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(stockTickerWebSocketHandler, "/stockticker").setAllowedOrigins("*");
	}
}
