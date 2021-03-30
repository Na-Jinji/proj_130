# 1. Introduction
## 이짝워뗘

- 개발기간 : 2020.12.26 ~ 2021.03.30
- 개발인원 : 3명
- 개발자들의 github 주소는  [ People ](#-8.-People-) 에서 확인할 수 있습니다.


### **[ 프로그램 소개 ]**

이짝워뗘는 경주 관광지에 대한 정보 제공 및 추천 시스템을 제공해주는 애플리케이션입니다. 

사용자는 카카오계정을 통해서 로그인과 로그아웃을 진행할 수 있고, 검색 시스템을 통해 입력한 키워드가 포함된 관광지 결과를 얻을 수 있습니다. 관광지를 선택하게 되면 해당 관광지와 유사한 5개의 관광지를 추천받을 수 있고 각 관광지에 대한 관련 정보를 볼 수 있습니다. 그리고 관광지에 대한 북마크를 진행할 수도 있고 이를 북마크 탭에서 따로 볼 수 있습니다. 

이 외에도 사용자 정보 편집 기능과 관광지에 대한 정보를 공유하는 기능을 제공하고 있습니다.


### [ 프로그램 흐름도 ]
![image](https://user-images.githubusercontent.com/60126234/112986007-eb62a100-919b-11eb-8370-f98e19dbdc3c.png)


# 2. Development Stack
- Python 3.7
- Flask
- Android Studio
- Docker
- Spring Boot
- MySQL
- Nginx


# 3. Features
### Search & Display

처음 화면에서 경주 관광지를 검색하면 다음과 같이 관련 관광지 목록이 뜹니다.
<p float="left">
    <img src="https://user-images.githubusercontent.com/53392870/112745585-831d8f00-8fe4-11eb-98e1-1e53d4360530.png" width="30%">
    <img src="https://user-images.githubusercontent.com/53392870/112745590-8fa1e780-8fe4-11eb-8dcd-e2d47daea7fb.png" width="30%">
</p>

관광지와 유사한 관광지 5곳을 추천합니다.

<img src="https://user-images.githubusercontent.com/53392870/112745597-9a5c7c80-8fe4-11eb-8c70-395648cbe3f7.png" width="30%">

관광지 중 한 곳을 선택하면 다음과 같이 관광지 관련 정보를 볼 수 있습니다.
<p float="left">
    <img src="https://user-images.githubusercontent.com/53392870/112745601-a21c2100-8fe4-11eb-9e76-2006c4f0cd54.png" width="30%">
    <img src="https://user-images.githubusercontent.com/53392870/112745603-a9432f00-8fe4-11eb-8359-c1ba94c9cc2b.png" width="30%">
</p>

관광지 페이지에서 공유 버튼을 클릭하면 다음과 같이 공유할 수 있고, 공유 화면은 앱 내의 관광지 정보를 그대로 담은 웹 페이지입니다.
<p float="left">
    <img src="https://user-images.githubusercontent.com/53392870/112745610-b19b6a00-8fe4-11eb-9705-f128148057f8.png" width="30%">
    <img src="https://user-images.githubusercontent.com/53392870/112746538-58363980-8fea-11eb-8377-e389ba22e462.png" width="30%">
</p>

### Bookmark

관광지 페이지에서 북마크 등록 및 해제를 할 수 있습니다.
<p float="left">
    <img src="https://user-images.githubusercontent.com/53392870/112745613-b95b0e80-8fe4-11eb-8ec5-8620047a7c3a.png" width="30%">
    <img src="https://user-images.githubusercontent.com/53392870/112746617-ddb9e980-8fea-11eb-8156-1a193580a37f.png" width="30%">
</p>

북마크를 등록하게 되면 북마크 탭에서 북마크 등록 현황을 확인할 수 있습니다. 북마크를 2초 이상 꾹 누르면 북마크 탭에서도 북마크를 해제할 수 있습니다.
<p float="left">
    <img src="https://user-images.githubusercontent.com/53392870/112745617-c1b34980-8fe4-11eb-846d-06fc60e6f625.png" width="30%">
    <img src="https://user-images.githubusercontent.com/53392870/112745631-e27b9f00-8fe4-11eb-88ea-758c7edfc91c.png" width="30%">
</p>

### MyPage
마이 페이지 탭을 클릭하면 내 정보를 볼 수 있습니다. My 편집을 클릭하면 나이, 주소, 성별을 수정할 수 있습니다.
<p float="left">
    <img src="https://user-images.githubusercontent.com/53392870/112745642-f7583280-8fe4-11eb-93ef-769249f100f1.png" width="20%">
    <img src="https://user-images.githubusercontent.com/53392870/112745644-f9ba8c80-8fe4-11eb-8dd9-0f6cb6d4df3c.png" width="20%">
</p>

# 4. Recommendation System
경주 관광지 추천 시스템은 사용자가 하나의 관광지명을 입력하면 이와 유사한 5개의 경주 관광지들을 추천해주는 추천 시스템입니다. 

## [ 추천 시스템 서버 ]

추천 시스템 서버는 Flask로 개발하였고, 앱 서버로 관광지 추천 요청이 들어오면 앱 서버가 추천 시스템 서버에 요청하도록 구현하였습니다.
* 경주 관광지 추천 시스템 : https://github.com/Na-Jinji/ai-server

<br>

## [ 추천 시스템 로직 ]
각각 경주 관광지의 특징들을 명사(noun), 대명사(pronoun), 동사(verb)로 표현한 특징 리스트를 생성하여 data.csv 파일로 저장하였습니다. 
추천 시스템을 생성할 때 csv 파일에서 '카테고리(category)', '관광지명(title)', '특징 리스트(tag)'를 추출합니다.
관광지마다 해당 카테고리와 특징 리스트를 하나의 text 문장으로 합친 뒤에 **Scikit-Learn의 CountVectorizer**를 통하여 문장 간 유사도를 구합니다. 
*CountVectorizer*는 문서 목록에서 각 문서의 feature (문장의 특징) 노출수를 가중치로 설정한 BOW(Bag Of Words) 벡터를 생성하는 API입니다.
*CountVectorizer의 fit_tranform*을 호출하여 각 관광지의 문장에서 노출되는 feature 수를 합한 DTM(Document Term Matrix)를 생성합니다. 여기서 경주 관광지들의 DTM은 shape가 (330, 878)입니다. 이 DTM을 이용하여 코사인 유사도(cosine similarity)를 구하면 get_recommendations이 호출될 때마다 코사인 유사도 중 해당 관광지명 인덱스에 해당하는 값에서 추천 개수만큼 뽑아냅니다. 이때 본인 관광지는 제외됩니다. 

```python
    class Model:
        def __init__(self, tokenizer=None):
            gyeongju_data = pd.read_csv('model/data.csv')
            self.metaData = gyeongju_data[['category', 'title', 'tag']].drop_duplicates()
            self.metaData['soup'] = self.metaData.apply(create_soup, axis=1)

            # 이름:index - 예) 로라커피:0, 이스트앵글:1
            self.indices = pd.Series(self.metaData.index, index=self.metaData['title']).drop_duplicates()

            # BOW 인코딩
            if tokenizer:
                count = CountVectorizer(analyzer='word', tokenizer=tokenizer.morphs)
            else:
                count = CountVectorizer(analyzer='word')
            count_matrix = count.fit_transform(self.metaData['soup'])

            # 코사인 유사도 구하기
            self.cosine_sim2 = cosine_similarity(count_matrix, count_matrix)

            # index 초기화
            self.metaData = self.metaData.reset_index()
            self.indices = pd.Series(self.metaData.index, index=self.metaData['title'])
```
<br>

### get_recommendations의 기본 로직은 이렇습니다.
1. 관광지명이 들어오면 관광지 이름을 가지고 있는 index를 뽑아냅니다. 
2. 코사인 유사도 중 관광지 이름에 해당하는 리스트인 sim_scores를 추출합니다. 
3. sim_scores를 내림차순으로 정렬합니다.
4. sim_scores에서 본인을 제외한 상위 5개를 뽑아낸 뒤 해당 score의 관광지명들을 리턴합니다. 

```python
   # 5개의 추천리스트 가져오기
    def get_recommendations(self, title):
        title = self.set_exact_title(title)
        if len(title) <= 0 or title == '':
            return []

        idx = self.indices[title]
        sim_scores = list(enumerate(self.cosine_sim2[idx]))  # 유사도 측정
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)  # 내림차순

        sim_scores = sim_scores[1:6]  # 5개
        attraction_indices = [i[0] for i in sim_scores]     # 장소 index
        scores = [i[1] for i in sim_scores]     # 유사도

        # debug
        for i in scores:
            print(i)

        result_data = self.metaData[['title', 'tag']].iloc[attraction_indices]
        result_data['scores'] = np.array(scores)
        return result_data['title'].values.tolist()
```
<br>

# 5. Server
애플리케이션 서버는 Spring Boot로 개발하였고, MySQL을 사용하였습니다. AWS EC2, RDS, S3를 이욯하고 있으며, Docker로 서버를 배포하였습니다. 이미지의 경우 s3에 저장해서 외부 접근을 허용하였습니다.

서버 구조는 아래의 [[ 애플리케이션 서버 구조 ]](#-애플리케이션-서버-구조-)와 같고 애플리케이션 서버에서 처리하는 주된 기능은 다음과 같습니다. 

추천 시스템 서버은 [[ 추천 시스템 서버 ]](#-추천-시스템-서버-)를 참고하세요.

* 애플리케이션 서버: https://github.com/nayoon-kim/ohmymoney-server

[ 사용자 ]
- 사용자 회원가입 및 로그인
- 사용자 정보 업데이트

[ 관광지 ]
- 추천 관광지 목록 요청
- 관광지 목록 가져오기 및 관광지 정보 표시

[ 북마크 ]
- 북마크 저장 및 삭제

## [ 애플리케이션 서버 구조 ]

<img src="https://user-images.githubusercontent.com/53392870/112744796-10111a00-8fde-11eb-8df3-4565910be6ba.png">

# 6. Source
* 경주 관광지의 이미지들은 구글 썸네일 이미지로부터 수집하였습니다.
* 경주 관광지의 카테고리(카페,명소,음식), 관광지명, 주소, 위치 정보(위도,경도), 전화번호, 홈페이지주소, 상세정보는 경주시청에서 운영하는 경주문화관광 웹사이트에서 수집하였습니다. 
>  경주문화관광 : https://www.gyeongju.go.kr/tour/index.do
<br><br>

# 7. Youtube


# 8. People
- 김나윤: https://github.com/nayoon-kim
- 김진희: https://github.com/jinhee19
- 설지우: https://github.com/jeewoo1025
