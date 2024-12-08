def distance_too_big?(arr)
    arr
        .each_cons(2)
        .map { |a, b| [a.to_i, b.to_i] }
        .any? { |a, b| (a - b).abs > 3 || (a - b).abs < 1 }
end

def all_increasing?(arr)
    arr
        .each_cons(2)
        .map {|a, b| [a.to_i, b.to_i] }
        .all? { |a, b| a < b }
end

def all_decreasing?(arr)
    arr
        .each_cons(2)
        .map {|a, b| [a.to_i, b.to_i] }
        .all? { |a, b| a > b }
end

def report_save?(report)
    (all_increasing?(report) || all_decreasing?(report)) && !distance_too_big?(report)
end

def read_report(file_name)
    File
        .foreach(file_name)
        .select { |report| report_save?(report.split(" ")) }
        .size
end

puts read_report("reports.txt")
