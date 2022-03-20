# Github Action 빌드 결과 Telegram Bot으로 보내기

팀 단위의 프로젝트에서는 Github Action 빌드의 결과를 Slack으로 보내겠지만,  
개인적인 프로젝트에서는 [Telegram](https://telegram.org/)을 사용할때가 많습니다.  
  
  
> 앞의 CI 과정과 Test Reports 추출 과정은 [이전 포스팅](https://jojoldu.tistory.com/602)을 참고하면 좋습니다. 

[telegram-action](https://github.com/appleboy/telegram-action)

```yaml
      # 빌드 실패 메세지 발송
      - name: Send Build Message
        uses: appleboy/telegram-action@master
        with:
          to: ${{ secrets.TELEGRAM_CHAT_ID }}
          token: ${{ secrets.TELEGRAM_TOKEN }}
          message: |
            ${{ github.actor }} created commit:
            Commit message: ${{ github.event.commits[0].message }}
            
            Repository: ${{ github.repository }}
            
            See changes: https://github.com/${{ github.repository }}/commit/${{github.sha}}
```

* `https://api.telegram.org/bot봇토큰/getUpdates` 
  * `bot`과 `봇토큰` 을 합쳐야 한다