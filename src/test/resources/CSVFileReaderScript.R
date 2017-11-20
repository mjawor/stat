# Title     : CSV reader
# Objective : TODO
# Created by: mjawor
# Created on: 2017-04-28

csv <- read.csv(file = "src/test/resources/test.csv", sep = ";");
table <- table(csv);
test <- chisq.test(table);
returnedResult <- test$p.value;
