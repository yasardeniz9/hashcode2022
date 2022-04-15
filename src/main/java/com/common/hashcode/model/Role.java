package com.common.hashcode.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Role {

  String skillName;
  Integer level;
  Contributor contributor;

  public Role(String skillName, Integer level) {
    this.skillName = skillName;
    this.level = level;
  }

  public void addContributor(Contributor contributor){
    this.contributor = contributor;
  }

}
