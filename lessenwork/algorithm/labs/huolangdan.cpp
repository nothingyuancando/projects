//
// Created by caiyuan on 24-10-13.
//

#include <bits/stdc++.h>
using namespace std;

const int N = 100;
const int INF = 0x3f3f3f3f;
int c[N][N];
int g[N][N];
int cnt[N];
int n;
int start;

int imin(int sum,int cur) {

if (n == 1) return g[cur][start];

   int mincost = INF;

for (int i = 0;i<n;i++){
if(!cnt[i] && i != start) {
cnt[i] = 1;
int val = g[cur][i] + imin(sum-1,i);
    if(val < mincost) mincost = val;
}
    cnt[i] = 0;
}
    return mincost;

}

int main(){
    cin>>n;
    cin>>start;
    cnt[start] = 1;
    for (int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            cin>>c[i][j];
            g[i][j] = c[i][j];
        }
    }

    cout<<imin(n,start) <<endl;
    return 0;
}