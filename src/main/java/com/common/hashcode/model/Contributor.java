package com.common.hashcode.model;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contributor {

  String name;
  HashMap<String, Integer> skillMap;
  boolean isAssigned = false; //this field is being changed in Project after assignings

  public Contributor(String name) {
    this.name = name;
    this.skillMap = new HashMap<>();
  }

  public void addSkill(String skillName, Integer level) {
    skillMap.put(skillName, level);
  }

  public Integer getSkillLevel(String skillName) {
    return this.skillMap.containsKey(skillName) ? this.skillMap.get(skillName) : 0;
  }

  /**
   * Not possessing a skill is equivalent to possessing a skill at level 0. So a contributor can
   * work on a project and be assigned to a role with requirement C++ level 1 if they don’t know any
   * C++, provided that somebody else on the team knows C++ at level 1 or higher.
   *
   * @param skillName
   */
  public void improveSkill(String skillName) {
    skillMap.put(skillName, skillMap.getOrDefault(skillName, 0) + 1);
  }
}
