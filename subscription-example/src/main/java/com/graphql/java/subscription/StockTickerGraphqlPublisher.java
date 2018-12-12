package com.graphql.java.subscription;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.graphql.java.subscription.data.StockTickerPublisher;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class StockTickerGraphqlPublisher {

	@Autowired
	private StockTickerPublisher stockTickerPublisher;

	private DataFetcher<?> stockQuotesSubscriptionFetcher() {
		return environment -> {
			List<String> arg = environment.getArgument("stockCodes");
			List<String> stockCodesFilter = arg == null ? Collections.emptyList() : arg;
			if (stockCodesFilter.isEmpty()) {
				return stockTickerPublisher.getPublisher();
			} else {
				return stockTickerPublisher.getPublisher(stockCodesFilter);
			}
		};
	}

	public GraphQLSchema getGraphQLSchema() {
		//
		// reads a file that provides the schema types
		//
		Reader streamReader = loadSchemaFile("stocks.graphqls");
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(streamReader);

		RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring("Subscription").dataFetcher("stockQuotes", stockQuotesSubscriptionFetcher()))
				.build();

		return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
	}

	private Reader loadSchemaFile(String name) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
		return new InputStreamReader(stream);
	}

}
