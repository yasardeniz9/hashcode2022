package com.common.hashcode.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Project {

  String name;
  Integer duration;
  Integer completionScore;
  Integer bestBeforeDay;
  List<Role> roles = new LinkedList<>();
  int lastScore = 0;

  public Project(String name) {
    this.name = name;
  }

  public Project(String name, Integer duration, Integer completionScore,
      Integer bestBeforeDay) {
    this.name = name;
    this.duration = duration;
    this.completionScore = completionScore;
    this.bestBeforeDay = bestBeforeDay;
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  /**
   * A contributor can be assigned to a project for a specific role (at most one role in a single
   * project), if they either: have the skill at the required level or higher; or have the skill at
   * exactly one level below the required level, only if another contributor on the same project
   * (assigned to another role), has this skill at the required level or higher. In this case, the
   * contributor will be mentored by their colleague :)
   *
   * @param contributor
   * @param role
   * @return True or False
   */
  public boolean isContributorApplicableToRole(Contributor contributor, Role role) {
    if (oneLevelBelowAndHasOtherContributorAtRequiredLevel(contributor, role) ||
        hasRequiredLevel(contributor, role)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Remove assignments of contributors and improve their related skill level.
   * <p>
   * contributors working in roles where the required skill level was equal or higher than their
   * current level improve their skill level by one level
   *
   * @return released contributors
   */
  public List<Contributor> releaseAndImproveContributors() {
    List<Contributor> releasedContributors = new ArrayList<>();
    roles.forEach(r -> {
      r.getContributor().setAssigned(false);
      releasedContributors.add(r.getContributor());
      if (r.level >= r.getContributor().getSkillLevel(r.getSkillName())) {
        r.getContributor().improveSkill(r.getSkillName());
      }
    });
    return releasedContributors;
  }

  /**
   * calculate the last score of project.
   *
   * @param day
   * @return last score
   */
  public int calculateScore(int day) {
    int score = 0;
    if (day <= this.bestBeforeDay) {
      score = completionScore;
    } else {
      score = completionScore - (day - bestBeforeDay);
    }
    return score > 0 ? score : 0;
  }

  /**
   * Decreasing the remaining time to finish the project.
   *
   * @return the remaining day count to finish project
   */
  public int decreaseAndGetDuration() {
    this.duration--;
    return this.duration;
  }

  private boolean hasRequiredLevel(Contributor contributor, Role role) {
    return contributor.getSkillLevel(role.getSkillName()) >= role.level;
  }

  private boolean oneLevelBelowAndHasOtherContributorAtRequiredLevel(Contributor contributor,
      Role role) {
    if (contributor.getSkillLevel(role.getSkillName()) == role.level - 1) {
      for (Role r : roles) {
        if (r.contributor != null
            && r.getContributor().getSkillLevel(role.skillName) >= role.getLevel()) {
          return true;
        }
      }
    }
    return false;
  }

}
