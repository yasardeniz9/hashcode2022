package com.common.hashcode;

import com.common.hashcode.model.Project;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemResult {
  List<Project> plannedProjects;
  int totalScore=0;

  public ProblemResult(List<Project> plannedProjects) {
    this.plannedProjects = plannedProjects;
  }

  public ProblemResult(List<Project> plannedProjects, int totalScore) {
    this.plannedProjects = plannedProjects;
    this.totalScore = totalScore;
  }

  public int calculateTotalScore(){
    plannedProjects.forEach(p->this.totalScore += p.getLastScore());
    return this.totalScore;
  }

}
