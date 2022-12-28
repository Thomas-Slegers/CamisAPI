package com.cegeka.horizon.camis.timesheet;

import com.cegeka.horizon.camis.domain.ResourceId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Employee {
    private ResourceId resourceId;
    private String name;
    private List<WeeklyTimesheet> weeklyTimeSheets = new ArrayList<>();

    public Employee(ResourceId resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

    public void addWeeklyTimesheet(WeeklyTimesheet weeklyTimesheetToAdd){
        Optional<WeeklyTimesheet> sameStart = this.weeklyTimeSheets.stream()
                        .filter(weeklyTimesheet -> weeklyTimesheet.startDate().equals(weeklyTimesheetToAdd.startDate())).findFirst();

        if(sameStart.isEmpty()){
            this.weeklyTimeSheets.add(weeklyTimesheetToAdd);
        }else{
            weeklyTimesheetToAdd.lines().forEach(line -> sameStart.get().addLine(line));
        }
    }

    public List<WeeklyTimesheet> weeklyTimesheets() {
        return this.weeklyTimeSheets.stream().sorted(new WeeklyTimesheet.SortByStartDate()).toList();
    }

    public List<WorkOrderStart> getFirstUseOfWorkOrders() {
        return this.weeklyTimeSheets.stream().map(weeklyTimesheet -> weeklyTimesheet.getFirstUseOfWorkOrders())
                .collect(Collectors.flatMapping(List::stream, Collectors.toList()));
    }

    public ResourceId resourceId() {
        return resourceId;
    }

    public String name() {
        return name;
    }

    public static class mergeFunction implements java.util.function.BiFunction<Employee, Employee, Employee> {
        @Override
        public Employee apply(Employee employee1, Employee employee2) {
            if(! employee1.resourceId.equals(employee2.resourceId)){
                throw new IllegalArgumentException("Only employees with same resourceId can be merged");
            }
            Employee mergedEmployee = new Employee(employee1.resourceId, employee1.name);
            employee2.weeklyTimeSheets.forEach(
                    mergedEmployee::addWeeklyTimesheet
            );
            employee1.weeklyTimeSheets.forEach(
                    mergedEmployee::addWeeklyTimesheet
            );
            return mergedEmployee;
        }
    }

    @Override
    public String toString() {
        return "Employee{" + "resourceId=" + resourceId +
                ", weeklyTimeSheets=" + weeklyTimeSheets +
                '}';
    }

    public static class SortByName implements java.util.Comparator<Employee> {
        @Override
        public int compare(Employee o1, Employee o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}