package com.arwest;

import com.datasonnet.Mapper;
import com.datasonnet.document.Document;
import com.datasonnet.document.StringDocument;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import groovy.json.JsonOutput;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import groovy.json.JsonSlurper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(2)
@State(Scope.Thread)
public class JsonBenchmark {

    String file;
    Gson gson;
    ObjectMapper objectMapper;
    JsonFactory jsonFactory;
    String employeeSkillResponseJson;
    String skillResponseJsonString;
    String dataSonnetScript;
    JsonSlurper jsonSlurper;
    String data;


    @Setup
    public void prepare() throws IOException {

        file  = new String(Files.readAllBytes(Paths.get("src/main/resources/bank_loan.json")));
        employeeSkillResponseJson = new String(Files.readAllBytes(Paths.get("src/main/resources/employees_objO.json")));
        skillResponseJsonString = new String(Files.readAllBytes(Paths.get("src/main/resources/employees_objT.json")));
        dataSonnetScript = new String(Files.readAllBytes(Paths.get("src/main/resources/employeeSkillById.ds")));

        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getFactory();
        gson = new Gson();
        jsonSlurper = new JsonSlurper();
        data = String.valueOf(jsonSlurper.parse(new File("src/main/resources/bank_loan.json")));
    }
    @Benchmark
    public void slurperGroovyParseJsonString(Blackhole blackhole){
        blackhole.consume(jsonSlurper.parseText(file));
    }
    @Benchmark
    public void slurperGroovyCreatingPrettyJsonString(Blackhole blackhole){
        blackhole.consume(JsonOutput.prettyPrint(file));
    }
    @Benchmark
    public void slurperGroovyCreatingJsonString(Blackhole blackhole){
        blackhole.consume(JsonOutput.toJson(file));
    }
//    @Benchmark
//    public void slurperGroovyReadJsonFromFile(Blackhole blackhole) throws IOException {
//        blackhole.consume(jsonSlurper.parse(Paths.get(file)));
//    }
    // : File name too long java.nio.file.FileSystemException:

    @Benchmark
    public void streaming(Blackhole blackhole) throws IOException {
        try (final JsonParser parser = jsonFactory.createParser(file)) {
            JsonToken token;
            while ((token = parser.nextToken()) != null) {
                if (token.isScalarValue()) {
                    final String currentName = parser.getCurrentName();
                    if (currentName != null) {
                        final String text = parser.getText();
                        blackhole.consume(text);
                    }
                }
            }
        }
    }

    @Benchmark
    public BasicLoanApplication binding() throws IOException {
        return objectMapper.readValue(file, BasicLoanApplication.class);
    }
    @Benchmark
    public JsonNode domApi () throws IOException {
        return objectMapper.readTree(file);
    }
    @Benchmark
    public BasicLoanApplication gson() {
        return gson.fromJson(file, BasicLoanApplication.class);
    }

    @Benchmark
    public String dataSonnet() throws IOException {

        Map<String, Document> variables = new HashMap<>();
        variables.put("objO", new StringDocument(skillResponseJsonString, "application/json"));
        variables.put("objT", new StringDocument(employeeSkillResponseJson, "application/json"));

        String dataSonnetScript = new String(Files.readAllBytes(Paths.get("src/main/resources/employeeSkillById.ds")));

        Mapper mapper = new Mapper(dataSonnetScript, variables.keySet());
        Document transformedResult = mapper.transform(new StringDocument(skillResponseJsonString,
                "application/json"), variables, "application/json");

        return transformedResult.getContentsAsString();
    }

    @Benchmark
    public String  readFile() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/bank_loan.json")));

    }


}