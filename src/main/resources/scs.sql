#! /bin/bash
for i in {1901..2017};
    do curl https://www.baseball-reference.com/leagues/MLB/$i-transactions.shtml > $i-transactions.shtml
done
