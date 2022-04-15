package com.common.hashcode;

import java.util.Map;

public class MainApplication {

  public static void main(String[] args) {
    Map<String, ProblemSpace> fileMap = FileOperation.getFileInput();

    fileMap.forEach((fileName, problemSpace) -> {

      ProblemSolution problemSolution = new ProblemSolution(fileName, problemSpace);
      Thread thread = new Thread(problemSolution);
      thread.start();
    });

  }

}
