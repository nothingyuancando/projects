#include<bits/stdc++.h>
using namespace std;
const int N = 20;
int n,y;
int a[N];
int t[N];
int rw = 0;
int counts = 0;

void output() {
    counts +=1 ;
    cout<<"the NO."<<counts<<" answer"<<endl;
    for(int i = 1; i <= n; i++)
        if(t[i] == 1) cout<< a[i]  <<" ";
    cout<<endl;
}
void dfs(int cnt,int rw,int i) {
if(i > n){if(cnt == y) output();}
    else {
        if(cnt + a[i] <= y){ t[i] = 1,dfs(cnt + a[i],rw-a[i],i+1); }
        if(cnt + rw > y) {
        t[i] = 0;dfs(cnt,rw - a[i],i+1);
        }
    }
}
int main(){
cin>>n>>y;
    for(int i=1;i<=n;i++) {
        cin>>a[i];
        rw += a[i];
    }
    dfs(0,rw,1);
return 0;
}

