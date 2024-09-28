import { defineStore } from 'pinia';
import apiClient from '../config/authConfig';

export const useNavAlarmStore = defineStore('navAlarm', {
  state: () => ({
    navAlarmList: [],
    alarmCount: 0,
  }),
  actions: {
    async getTopNavAlarmList() {
      try {
        const response = await apiClient.get(`/main/alarm/nav`);
        this.navAlarmList = response.data.result;
        this.alarmCount = 0;

        for (let alarm of this.navAlarmList) {
          this.alarmCount += alarm.alarmCount;
        }

        console.log(this.alarmCount);
        console.log(this.navAlarmList);
      } catch (error) {
        console.log('에러가 발생했습니다.', error);
        alert('네비에 알람을 가져오는데 실패했습니다.');
      }
    },
  },
});
