package com.common.hashcode;

import com.common.hashcode.model.Contributor;
import com.common.hashcode.model.Project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class ProblemSpace {
  final List<Contributor> contributors = new ArrayList<>();
  final List<Project> projects = new ArrayList<>();
  //Map<String, List<Contributor>> skillContributorsMap = new HashMap<>();

  public void addContributor(Contributor contributor){
    contributors.add(contributor);
  }

  public void addProject(Project project){
    projects.add(project);
  }

}
