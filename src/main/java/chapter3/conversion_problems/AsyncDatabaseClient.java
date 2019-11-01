package chapter3.conversion_problems;

import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {
    <T> CompletionStage<T> store(CompletionStage<T> stage);
}
