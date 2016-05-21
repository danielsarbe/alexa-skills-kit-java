package com.example.other;

import com.lesstif.jira.issue.*;
import com.lesstif.jira.services.IssueService;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.xpath.SourceTree;

import java.io.IOException;
import java.util.List;

/**
 * Created by opetridean on 5/21/16.
 */
public class JiraApi {

    public static void main(String[] args) throws ConfigurationException, IOException {
//        createTask("Testing2", "opetridean");
        getOpenBugs("medium");
    }

    public static void createTask(String description, String userName) throws ConfigurationException, IOException {
        Issue issue = new Issue();

        IssueFields fields = new IssueFields();

        fields.setProjectKey("TEST")
                .setSummary(description)
                .setIssueTypeName(IssueType.ISSUE_TYPE_TASK)
                .setDescription(description)
                .setAssigneeName(userName);

        // Change Reporter need admin role
//        fields.setReporterName("")
//                .setPriorityName(Priority.PRIORITY_CRITICAL)
//                .setLabels(new String[]{"bugfix","blitz_test"})
//                .setComponentsByStringArray(new String[]{"Component-1", "Component-2"})
//                .addAttachment("readme.md")
//                .addAttachment("bug-description.pdf")
//                .addAttachment("screen_capture.png");

        issue.setFields(fields);


        IssueService issueService = new IssueService();

        Issue genIssue = issueService.createIssue(issue);

        String issueKey = genIssue.getKey();

        System.out.println(issueKey);
    }

    public static int getOpenBugs(String priority) throws ConfigurationException, IOException {

        IssueService issueService = new IssueService();
        IssueSearchResult issuesFromQuery = issueService.getIssuesFromQuery("issuetype = Bug AND priority=" + priority);

        System.out.println(issuesFromQuery.getIssues().size());
        return issuesFromQuery.getIssues().size();
    }


}

