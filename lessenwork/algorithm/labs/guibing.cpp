//
// Created by caiyuan on 2024/10/13.
//
#include<bits/stdc++.h>
using namespace std;

const int N = 10000;

    int n;
    int a[N];

void mergesort(int l, int r)
{
    if (l >= r) return;
    int temp[N];
    int mid = l+r>>1;
    mergesort(l, mid), mergesort(mid+1, r);
    int k = 0, i = l, j = mid+1;
    while (i <= mid && j <= r)
    {
        if (a[i] < a[j]) temp[k ++ ] = a[i ++ ];
        else temp[k ++ ] = a[j ++ ];

    }
    while (i <= mid) temp[k ++ ] = a[i ++ ];
    while (j <= r) temp[k ++ ] = a[j ++ ];
    for (int i = l, j = 0; i <= r; i ++ , j ++ ) a[i] = temp[j];
}


int main() {

    cin>>n;
    for(int i=1;i<=n;i++)cin>>a[i];
    mergesort(1,n);
    for(int i=1;i<=n;i++)cout<<a[i]<<" ";
    cout<<endl;
    return 0;
}