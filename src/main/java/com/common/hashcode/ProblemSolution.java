package com.common.hashcode;

import com.common.hashcode.model.Contributor;
import com.common.hashcode.model.Project;
import com.common.hashcode.model.Role;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class ProblemSolution implements Runnable {

  String fileName;
  ProblemSpace problemSpace;

  public ProblemSolution(String fileName, ProblemSpace problemSpace) {
    this.fileName = fileName;
    this.problemSpace = problemSpace;
  }

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName());
    System.out.println(Thread.currentThread().getName() + "Input File Name: " + fileName);
    System.out.println(Thread.currentThread().getName() + "Contributor Count = "
        + problemSpace.contributors.size());
    System.out.println(
        Thread.currentThread().getName() + "Project Count = " + problemSpace.projects.size());

    int workingDay = 0;
    List<Contributor> contributors = problemSpace.getContributors();
    //TODO: sort options for contributors can improve scores
    Collections.sort(contributors, (c1, c2) -> c1.getSkillMap().size() - c2.getSkillMap().size());

    //good scores by sorting to completion Score
    PriorityQueue<Project> projectQueue = new PriorityQueue<>(
        (a, b) -> b.getCompletionScore() - a.getCompletionScore());

    //not good scores at all
    /*PriorityQueue<Project> projectQueue = new PriorityQueue<>(
        (a, b) -> a.getDuration() - b.getDuration());*/

    //File E score increased with Best Before Day sorting
    /*PriorityQueue<Project> projectQueue = new PriorityQueue<>(
        (a, b) -> a.getBestBeforeDay() - b.getBestBeforeDay());*/

    projectQueue.addAll(problemSpace.getProjects());

    List<Project> assignedProjects = new ArrayList<>();
    List<Project> completedProjects = new ArrayList<>();

    do {
      //check assigned projects and remove completed one's by adding them to final completed list.
      //also add released contributors back to default contributor list
      int assignedProjectCount = assignedProjects.size();
      completedProjects.addAll(
          getCompletedProjectsAndRemoveThemFromAssigned(assignedProjects, workingDay,
              contributors));
      //if it is not first cycle and there is not any completed project,
      //there is no need to try planning new project for performance improvement
      boolean shouldTryToPlanNewProject =
          assignedProjects.size() == 0 || (assignedProjectCount - assignedProjects.size() > 0);
      List<Project> unAssignedProjects = new ArrayList<>();
      Project project;

      while (shouldTryToPlanNewProject && ((project = projectQueue.poll()) != null)) {
        if (planProject(project, contributors)) {
          assignedProjects.add(project);
        } else {
          //if project score will be 0 skip the project, don't need to add it again to queue.
          if (project.calculateScore(workingDay - 1) > 0) {
            unAssignedProjects.add(project);
          }
        }
      }
      projectQueue.addAll(unAssignedProjects); //add unassigned projects into queue for next cycle
      workingDay++;
      //System.out.println("Working day: " + workingDay);
    } while (!assignedProjects.isEmpty());

    ProblemResult problemResult = new ProblemResult(completedProjects);
    int totalScore = problemResult.calculateTotalScore();
    FileOperation.writeFileOutput(fileName, problemResult);
    System.out.println(Thread.currentThread().getName());

    System.out.println("Completed Project Count = " + completedProjects.size());
    System.out.println("Total Score = " + totalScore);
  }

  private List<Project> getCompletedProjectsAndRemoveThemFromAssigned(
      List<Project> assignedProjects, int workingDay, List<Contributor> contributors) {
    List<Project> completedProjects = new ArrayList<>();
    for (Project p : assignedProjects) {
      if (p.decreaseAndGetDuration() == 0) {
        List<Contributor> releasedContributors = p.releaseAndImproveContributors();
        p.setLastScore(p.calculateScore(workingDay));
        contributors.addAll(releasedContributors);
        completedProjects.add(p);
      }
    }
    assignedProjects.removeAll(completedProjects);
    return completedProjects;
  }

  /**
   * Try to plan project.
   *
   * @param project
   * @param contributors
   * @return True if all role can be filled, False if any role can not be filled
   */
  private boolean planProject(Project project, List<Contributor> contributors) {
    Set<Contributor> addedContributors = new HashSet<>();
    for (Role role : project.getRoles()) {
      for (Contributor c : contributors) {
        if (!addedContributors.contains(c) && project.isContributorApplicableToRole(c, role)) {
          role.addContributor(c);
          c.setAssigned(true);
          addedContributors.add(c);
          break; // do not need to loop for other contributors
        }
      }
      if (role.getContributor() == null) {
        //clear assigning (TODO:should be improved)
        project.getRoles().stream().forEach(r -> r.setContributor(null));
        addedContributors.forEach(c -> c.setAssigned(false));
        return false;
      }
    }
    contributors.removeAll(addedContributors);
    return true;
  }
}
